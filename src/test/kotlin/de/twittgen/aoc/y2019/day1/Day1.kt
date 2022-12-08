package de.twittgen.aoc.y2019.day1


import de.twittgen.aoc.util.FileUtil.readInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Day1{
    val input = readInput("2019/day1")
    @Test
    fun part1() {
        val result = input.toRocket().getFuelNeeded()
        println(result)
        assertEquals(3331849, result)
    }

    @Test
    fun part2() {
        val result = input.toRocket().getFuelNeededRecursive()
        println(result)
        assertEquals(4994898, result)
    }

    private fun String.toRocket() : Rocket = Rocket(filterNot { it =='\r' }.split("\n").map { it.toInt(10) })
}