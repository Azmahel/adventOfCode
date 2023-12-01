package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.emptyLine

class Day01 : Day<List<Int>>() {
    override fun String.parse() = split(emptyLine).map { it.lines().sumOf(String::toInt) }
    init {
        part1(24000, 71780) { it.maxOrNull()!! }
        part2(45000, 212489) { it.sortedDescending().take(3).sum() }
    }

    override val example = """
        1000
        2000
        3000

        4000

        5000
        6000

        7000
        8000
        9000

        10000
    """.trimIndent()
}