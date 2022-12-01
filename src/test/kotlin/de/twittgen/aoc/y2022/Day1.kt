package de.twittgen.aoc.y2022

import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day1 {
    val input  by lazy { parseInput(FileUtil.readInput("2022/day1")) }
    val example = """
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

    fun parseInput(input: String) : List<Int> =
        input.split("\n\n")
            .map {
                it
                    .lines()
                    .map { it.toInt() }
            }.map { it.sum() }

    @Test
    fun part1Example() {
        val elfFood = parseInput(example)
        println(elfFood.maxOrNull())
        assertEquals(24000, elfFood.maxOrNull())
    }

    @Test
    fun part1() {
        val elfFood = input.maxOrNull()
        println(elfFood)
        assertEquals(71780, elfFood)
    }

    @Test
    fun part2Example() {
        val elfFood = parseInput(example)
        val topThree = elfFood
            .sortedDescending()
            .take(3)
            .sum()
        println(topThree)
        assertEquals(45000, topThree)
    }

    @Test
    fun part2() {
        val topThree = input
            .sortedDescending()
            .take(3)
            .sum()
        println(topThree)
        assertEquals(212489, topThree)
    }
}