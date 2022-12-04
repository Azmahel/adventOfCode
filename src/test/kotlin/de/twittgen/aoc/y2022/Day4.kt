package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

typealias Assignment = Pair<IntRange, IntRange>

class Day4  : Day<Int, Int, List<Assignment>> ({
    example = """
        2-4,6-8
        2-3,4-5
        5-7,7-9
        2-8,3-7
        6-6,4-6
        2-6,4-8
    """.trimIndent()
    part1 {
        exampleExpected = 2
        expected = 448
    }
    part2 {
        exampleExpected = 4
        expected = 794
    }
}){
    override fun part1(input: List<Assignment>): Int = input.count { it.hasCompleteOverlap() }

    override fun part2(input: List<Assignment>): Int = input.count { it.hasOverlap() }

    override fun parseInput(s: String) = s.lines().map { it.mapLine() }

    private fun String.mapLine() = split(',').map { it.parseRange() }.let { it[0] to it[1] }

    private fun String.parseRange() = split('-').let { it[0].toInt()..it[1].toInt() }

    private fun Pair<IntRange, IntRange>.hasCompleteOverlap() : Boolean {
      return if (first.first < second.first) {
           first.last >= second.last
       } else if (first.first > second.first) {
           second.last >= first.last
      } else {
          true
      }
    }

    private fun Pair<IntRange, IntRange>.hasOverlap() : Boolean = first.intersect(second).isNotEmpty()

}