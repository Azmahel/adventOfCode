package de.twittgen.aoc.y2024

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.second
import kotlin.math.abs
import kotlin.math.min

class Day01 : Day<Pair<List<Int>,List<Int>>>() {
    override fun String.parse() = lines()
        .map { it.split("   ")
            .let { it.first().toInt() to it.second().toInt()} }.toPairOfLists()

    init {
        part1(11, 1506483) { (a,b) ->
            (a.sorted() to b.sorted()).toListOfPairs().sumOf { (a, b) -> abs(a - b) }
        }
        part2(31,23126924) { (a,b) ->
          b.entryCount().let {counts  ->  a.sumOf { it * counts.getOrDefault(it, 0) } }
        }
    }

    private  fun <A, B> List<Pair<A,B>>.toPairOfLists() : Pair<List<A>,List<B>> {
        val (listA, listB)  = mutableListOf<A>() to mutableListOf<B>()
        forEach { (a,b) -> listA.add(a); listB.add(b) }
        return listA to listB
    }

    private  fun <A, B> Pair<List<A>,List<B>>.toListOfPairs() :  List<Pair<A,B>> =
        (0..min(first.size-1,second.size-1)).map { first[it] to second[it] }

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