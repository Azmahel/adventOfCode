package de.twittgen.aoc.y2021

import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day0 {
    val input by lazy { FileUtil.readInput("2021/day0").parse() }
    val example = """""".parse()

    private fun String.parse() = lines()


    @Test
    fun example() {
        val result = example
    }

    @Test
    fun example2() {
        val result = example
    }

    @Test
    fun part1() {
        val result = input
        println(result)
    }

    @Test
    fun part2() {
        val result = input
        println(result)
    }
}

