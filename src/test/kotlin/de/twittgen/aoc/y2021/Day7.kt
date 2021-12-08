package de.twittgen.aoc.y2021

import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.abs

class Day7 {
    val input by lazy { FileUtil.readInput("2021/day7").parse() }
    val example = """16,1,2,0,4,2,7,1,2,14""".parse()

    private fun String.parse() = split(",").map { it.toInt() }

    private fun List<Int>.getMean() = (sum().toDouble() / size)
    private fun List<Int>.getMedian() = sorted()[(size+1)/2]
    private fun List<Int>.getFuelForAlignment(i: Int) = sumOf { abs(it - i) }
    private fun List<Int>.getEscalatingFuelForAlignment(i: Int) = sumOf { (0..(abs(it - i ).toLong())).sum() }

    @Test
    fun example() {
        val result = example.getFuelForAlignment(example.getMedian())
        assertEquals(37, result)
    }

    @Test
    fun example2() {
        val mean = example.getMean()
        val result = listOf(
            example.getEscalatingFuelForAlignment(mean.toInt()),
            example.getEscalatingFuelForAlignment(mean.toInt()+1)
        ).minOrNull()!!

        assertEquals(168, result)
    }

    @Test
    fun part1() {
        val result = input.getFuelForAlignment(input.getMedian())
        println(result)
    }

    @Test
    fun part2() {
        val mean = input.getMean()
        val result = listOf(
            input.getEscalatingFuelForAlignment(mean.toInt()),
            input.getEscalatingFuelForAlignment(mean.toInt()+1)
        ).minOrNull()!!
        println(result)
    }
}

