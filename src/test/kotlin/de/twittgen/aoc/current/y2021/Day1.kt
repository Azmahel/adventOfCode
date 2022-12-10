package de.twittgen.aoc.current.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.second

class Day1 : Day<List<Int>>() {
    override fun String.parse() = lines().map { it.toInt() }

    init {
        part1(7, 1195) { getDepthIncreased().count { it } }
        part2(5, 1235) { to3PMeasurement().getDepthIncreased().count { it } }
    }

    private fun List<Int>.getDepthIncreased() = windowed(2).map{ it.first() < it.second() }
    private fun List<Int>.to3PMeasurement() = windowed(3,).map { it.sum() }

    override val example = """
        199
        200
        208
        210
        200
        207
        240
        269
        260
        263
    """.trimIndent()
}
