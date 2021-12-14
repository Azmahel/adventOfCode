package de.twittgen.aoc.y2021

import de.twittgen.aoc.y2019.shared.util.FileUtil
import de.twittgen.aoc.y2019.shared.util.second
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class Day14 {
    val input by lazy { FileUtil.readInput("2021/day14").parse() }
    val example = """NNCB

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
CN -> C""".parse()

    private fun String.parse() =
        lines().first().toList() to lines()
            .drop(2)
            .associate {
                it
                    .split(" -> ")
                    .let {
                        (with(it.first().toList()) { first() to second() }) to it.second().first()
                    }
            }


    private tailrec fun List<Char>.doSteps(insertions: Map<Pair<Char, Char>, Char>, i: Int): List<Char> {
        if (i == 0) return this
        return windowed(2, 1)
            .flatMap { (a, b) ->
                if (a to b !in insertions) {
                    listOf(a)
                } else {
                    listOf(a, insertions[a to b]!!)
                }
            }
            .plus(last()) //last char will always stay last
            .doSteps(insertions, i - 1)
    }

    private tailrec fun Map<Pair<Char, Char>, Long>.doSteps(
        insertions: Map<Pair<Char, Char>, Char>,
        i: Int,
    ): Map<Pair<Char, Char>, Long> {
        if (i == 0) return this
        val x = flatMap { (pair, count) ->
            if (pair !in insertions) {
                listOf(pair to count)
            } else {
                val insert = insertions[pair]!!
                listOf(
                    (pair.first to insert) to count,
                    (insert to pair.second) to count,
                )
            }
        }.groupBy { it.first }.mapValues { (_, v) -> v.sumOf { it.second } }.toMap()

        return x.doSteps(insertions, i - 1)
    }

    private fun List<Char>.toPairCount() = windowed(2, 1).map { (a, b) -> a to b }.let {
        it.associateWith { pair ->
            it.count { it == pair }.toLong()
        }
    }

    private fun List<Char>.score() = toSet().map { c -> count { it == c } }.let {
        it.maxOrNull()!! - it.minOrNull()!!
    }

    private fun Map<Pair<Char, Char>, Long>.score(initial: List<Char>) =
        flatMap { (k, v) -> listOf(k.first to v, k.second to v) }
            .groupBy { it.first }
            .mapValues { (k, v) ->
                v.sumOf { it.second }
                    //every char except first and last is in 2 pairs
                    .let { if (k in listOf(initial.first(), initial.last())) it + 1 else it }
                    .div(2)
            }
            .let {
                it.entries.maxByOrNull { it.value }!!.value - it.entries.minByOrNull { it.value }!!.value
            }

    @Test
    fun example() {
        val result = with(example) { first.doSteps(second, 10).score() }
        assertEquals(1588, result)
    }

    @Test
    fun example2() {
        val result = with(example) { first.toPairCount().doSteps(second, 40).score(first) }
        assertEquals(2188189693529, result)
    }

    @Test
    fun part1() {
        val result = with(input) { first.doSteps(second, 10).score() }
        println(result)
    }

    @Test
    fun part2() {
        val result = with(input) { first.toPairCount().doSteps(second, 40).score(first) }
        println(result)
    }
}


