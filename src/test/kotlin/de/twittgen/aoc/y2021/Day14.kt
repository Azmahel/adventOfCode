package de.twittgen.aoc.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.second
import kotlin.math.roundToLong

typealias Rules = Map<Pair<Char, Char>, Char>
typealias Polymer = List<Char>

class Day14 : Day<Int, Long, Pair<Polymer, Rules>>() {
    override fun String.parse(): Pair<Polymer, Rules> = lines().first().toList() to lines().drop(2).toRules()

    private fun List<String>.toRules() = associate {
        it.split(" -> ").run {
            first().run { get(0) to get(1) } to second().single() }
    }

    init {
        part1(1588, 2068) { let { (poly, rules) -> poly.transform(rules,10).score()  }}
        part2(2188189693529, 2158894777814) {
            let { (poly, rules) -> poly.toPairCount().transform(rules, 40).score() }
        }
    }

    private tailrec fun Polymer.transform(insertions: Rules, steps: Int): Polymer {
        if (steps == 0) return this
        return windowed(2, 1)
            .flatMap { (a, b) ->
                if (a to b !in insertions) {
                    listOf(a)
                } else {
                    listOf(a, insertions[a to b]!!)
                }
            }.plus(last())
            .transform(insertions, steps - 1)
    }

    private tailrec fun Map<Pair<Char, Char>, Long>.transform(insertions: Rules, i: Int, ): Map<Pair<Char, Char>, Long> {
        if (i == 0) return this
        return flatMap { (pair, count) ->
            if (pair !in insertions) {
                listOf(pair to count)
            } else {
                val insert = insertions[pair]!!
                listOf(
                    (pair.first to insert) to count,
                    (insert to pair.second) to count,
                )
            }
        }.groupBy { it.first }
            .mapValues { (_, v) -> v.sumOf { it.second } }
            .toMap()
            .transform(insertions, i - 1)
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


