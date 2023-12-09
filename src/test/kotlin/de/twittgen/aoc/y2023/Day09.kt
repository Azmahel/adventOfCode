package de.twittgen.aoc.y2023

import de.twittgen.aoc.Day

class Day09 : Day<List<List<Int>>>() {
    override fun String.parse() = lines().map { it.trim().split(" ").map(String::toInt) }

    init {
        part1(114, 1992273652) { readings -> readings.sumOf { it.predictNext() } }
        part2(2,1012) { readings -> readings.sumOf { it.reversed().predictNext() } }
    }

    private fun List<Int>.predictNext() : Int =
        if (all { it == 0 }) 0 else last() + dropLast(1).mapIndexed { i , it -> get(i+1) - it}.predictNext()

    override val example = """
        0 3 6 9 12 15
        1 3 6 10 15 21
        10 13 16 21 30 45
    """.trimIndent()
}