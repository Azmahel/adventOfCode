package de.twittgen.aoc.current.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.mapIf
import de.twittgen.aoc.util.second
import kotlin.math.roundToLong


class Day14 : Day<Int, Long, Pair<Polymer, Rules>>() {
    override fun String.parse(): Pair<Polymer, Rules> = lines().first().toList() to lines().drop(2).toRules()

    private fun List<String>.toRules() =
        associate { it.split(" -> ").run { first().run { get(0) to get(1) } to second().single() } }

    init {
        part1(1588, 2068) { let { (poly, rules) -> poly.transform(rules,10).score()  } }
        part2(2188189693529, 2158894777814) {
            let { (poly, rules) -> poly.toPairCount().transform(rules, 40).score() }
        }
    }

    private tailrec fun Polymer.transform(insertions: Rules, steps: Int): Polymer {
        if (steps == 0) return this
        return windowed(2, 1)
            .flatMap { (a, b) -> if (a to b !in insertions) listOf(a) else listOf(a, insertions[a to b]!!) }
            .plus(last())
            .transform(insertions, steps - 1)
    }

    private tailrec fun Map<Molecule, Long>.transform(insertions: Rules, steps: Int ): Map<Molecule, Long> {
        if (steps == 0) return this
        return entries.map { listOf(it.toPair())}
            .mapIf({ it.single().first in insertions }) { val (molecule, count) = it.single()
                insertions[molecule]!!.let { c ->  molecule.inject(c).map { m -> m to count } }
            }.flatten().groupBy { it.first }
            .mapValues { (_, v) -> v.sumOf { it.second } }
            .toMap()
            .transform(insertions, steps - 1)
    }

    private fun Polymer.toPairCount() = windowed(2, 1)
        .map { (a, b) -> a to b }
        .groupBy { it }
        .mapValues { (_,v) -> v.size.toLong() }

    private fun Polymer.score() = toSet().map { c -> count { it == c } }.let { it.maxOrNull()!! - it.minOrNull()!! }

    private fun Map<Pair<Char, Char>, Long>.score() =
        flatMap { (k, v) -> listOf(k.first to v, k.second to v) }
            .groupBy { it.first }
            .values
            .map{ v -> v.sumOf { it.second }.toDouble() / 2 }
            .map{ it.roundToLong() }
            .run { maxOrNull()!! - minOrNull()!! }

    override val example = """
        NNCB
        
        CH -> B
        HH -> N
        CB -> H
        NH -> C
        HB -> C
        HC -> B
        HN -> C
        NN -> C
        BH -> H
        NC -> B
        NB -> B
        BN -> B
        BB -> N
        BC -> B
        CC -> N
        CN -> C
    """.trimIndent()
}
private typealias Rules = Map<Molecule, Char>
private typealias Molecule = Pair<Char,Char>
private fun Molecule.inject(c: Char) = listOf(first to c, c to second)
private typealias Polymer = List<Char>



