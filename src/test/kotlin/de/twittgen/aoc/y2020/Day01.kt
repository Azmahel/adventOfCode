package de.twittgen.aoc.y2020

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.second

class Day01 : Day<List<Int>>() {
   override fun String.parse() = lines().map(String::toInt)

    init {
        part1(514579, 100419) {list ->  list.findSumTo(2020).run {  first() * second() } }
        part2(241861950, 265253940) {list ->  list.findThreeSum(2020).fold(1) { x, y -> x * y} }
    }

    private fun List<Int>.findThreeSum(i: Int) = filter { findSumTo(i - it).isNotEmpty() }.take(3)

    private fun List<Int>.findSumTo(i: Int) = filter { i - it in this }

    override val example = """
        1721
        979
        366
        299
        675
        1456
    """.trimIndent()
}

