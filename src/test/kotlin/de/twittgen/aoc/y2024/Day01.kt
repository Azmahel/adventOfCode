package de.twittgen.aoc.y2024

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.mapLines
import de.twittgen.aoc.util.second
import kotlin.math.abs
import kotlin.math.min

class Day01 : Day<Pair<List<Int>,List<Int>>>() {
    override fun String.parse() =
        mapLines { it.split("   ").let { it.first().toInt() to it.second().toInt()} }.toPairOfLists()

    init {
        part1(11, 2000468) { (a,b) ->
            (a.sorted() to b.sorted()).toListOfPairs().sumOf { (a, b) -> abs(a - b) }
        }
        part2(31,18567089) { (a,b) ->
            b.entryCount().let {counts  ->  a.sumOf { it * counts.getOrDefault(it, 0) } }
        }
    }

    private  fun <A, B> List<Pair<A,B>>.toPairOfLists() =
        (mutableListOf<A>() to mutableListOf<B>()).also { (lA, lB) ->  forEach { (a,b) -> lA.add(a); lB.add(b) }}
    private  fun <A, B> Pair<List<A>,List<B>>.toListOfPairs() :  List<Pair<A,B>> =
        (0..min(first.lastIndex,second.lastIndex)).map { first[it] to second[it] }
    private fun List<Int>.entryCount() = groupBy { it }.mapValues { (_, l) -> l.size }

    override val example = """
        3   4
        4   3
        2   5
        1   3
        3   9
        3   3
    """.trimIndent()
}