package de.twittgen.aoc.y2021

import de.twittgen.aoc.Day

class Day01 : Day<List<Int>>() {
    override fun String.parse() = lines().map { it.toInt() }

    init {
        part1(7, 1195) { d ->  d.getDepthIncreased() }
        part2(5, 1235) { d -> d.to3PMeasurement().getDepthIncreased() }
    }

    private fun List<Int>.getDepthIncreased() = windowed(2).count{(a,b) ->  a < b}
    private fun List<Int>.to3PMeasurement() = windowed(3).map { it.sum() }

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
