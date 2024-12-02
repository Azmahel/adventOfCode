package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.mapLines

class Day20 : Day<List<Long>>() {
    override fun String.parse() = mapLines(String::toLong)

    private val key = 811589153
    init {
        part1(3, 8302) { it.mix().score() }
        part2(1623178306, 656575624777) { it.map {i -> i *key }.mix(10).score() }
    }

    private fun List<Long>.mix(times: Int = 1): List<Long> {
        val indexed = mapIndexed { index, i -> index to i  }
        val mixed = indexed.toMutableList()
        repeat(times) {
            indexed.forEach { pivot ->
                val i = mixed.indexOf(pivot)
                mixed.removeAt(i)
                val insertAt = (i + pivot.second).mod(mixed.size)
                mixed.add(insertAt, pivot)
            }
        }
        return mixed.map { it.second }
    }

    private fun List<Long>.score() =
        indexOfFirst { it == 0L }.let { listOf(1000, 2000, 3000).sumOf { c -> get((it + c) % size) } }


    override val example = """
        1
        2
        -3
        3
        -2
        0
        4
    """.trimIndent()
}