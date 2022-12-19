package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day

class Day02: Day<List<String>>() {
    override fun String.parse() = lines()

    init {
        part1(15, 13924) {
            it.sumOf { listOf("B X", "C Y", "A Z", "A X", "B Y", "C Z", "C X", "A Y", "B Z").indexOf(it) + 1 }
        }
        part2(12, 13448) {
           it.sumOf { listOf("B X", "C X", "A X", "A Y", "B Y", "C Y", "C Z", "A Z", "B Z").indexOf(it) + 1 }
        }
    }

    override val example = """
        A Y
        B X
        C Z
    """.trimIndent()
}