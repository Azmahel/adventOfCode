package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day1 : Day<Int, Int, List<Int>>() {
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
    override fun String.parse() = split("\n\n").map { it.lines().sumOf(String::toInt) }
    init {
        part1(24000, 71780) {
            maxOrNull()!!
        }
        part2(45000, 212489) {
            sortedDescending().take(3).sum()
        }
    }
}