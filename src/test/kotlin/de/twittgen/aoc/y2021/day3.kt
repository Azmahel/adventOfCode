package de.twittgen.aoc.y2021

import de.twittgen.aoc.y2019.shared.util.FileUtil
import de.twittgen.aoc.y2019.shared.util.second
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class Day3 {
    val input = FileUtil.readInput("2021/day3").parse()
    val example = """00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010""".parse()

    private fun String.parse() = lines()

    private fun getGamma(report: List<String>): String {
        return (0 until report[0].length).map { index ->
            report.count { line -> line[index] == '1'}
        }.map {
            if(it.toDouble() >= (report.size/2.0)) 1 else 0
        }.joinToString("")
    }

    private fun getEpsilon(gamma:String) = gamma.map { if(it == '1') 0 else 1 }.joinToString("")

    @Test
    fun example() {
        val gamma = getGamma(example)
        assertEquals("10110", gamma)
        val epsilon = getEpsilon(gamma)
        assertEquals("01001", epsilon)
        val result = gamma.toInt(2) * epsilon.toInt(2)
        assertEquals(198, result)
    }

    @Test
    fun example2() {
        val oxygen = getRating(example, ::getOxygenRatingFunction)
        assertEquals("10111", oxygen)
        val co2 = getRating(example, ::getCo2RatingFunction)
        assertEquals("01010", co2)
        val result = oxygen.toInt(2) * co2.toInt(2)
        assertEquals(230, result)
    }

    @Test
    fun part1() {
        val gamma = getGamma(input)
        val epsilon = getEpsilon(gamma)
        val result = gamma.toInt(2) * epsilon.toInt(2)
        println(result)
    }

    @Test
    fun part2() {
        val oxygen = getRating(input, ::getOxygenRatingFunction)
        val co2 = getRating(input, ::getCo2RatingFunction)
        val result = oxygen.toInt(2) * co2.toInt(2)
        println(result)
    }

    private fun getOxygenRatingFunction(gamma: String) = { x: Char, i: Int  ->
        gamma[i] == x
    }

    private fun getCo2RatingFunction(gamma: String) = { x: Char, i: Int  ->
        gamma[i] != x
    }

    private fun getRating(report: List<String>, ratingFunction : (String) ->((Char, Int) -> Boolean)): String {
        var remaining = report
        var index = 0
        while(remaining.size > 1) {
            val gamma = getGamma(remaining)
            remaining = remaining.filter { ratingFunction(gamma)(it[index], index)}
            index ++
        }
        return remaining.first()
    }
}
