package de.twittgen.aoc.y2022

import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day4 {
    val input by lazy { parseInput(FileUtil.readInput("2022/day4")) }
    val example = """
        2-4,6-8
        2-3,4-5
        5-7,7-9
        2-8,3-7
        6-6,4-6
        2-6,4-8
    """.trimIndent()

    fun parseInput(s: String) = s
        .lines()
        .map { it.mapLine() }

    fun String.mapLine() =
        split(',')
        .map { it.parseRange() }
            .let { it[0] to it[1] }

    fun String.parseRange() = split('-')
        .let {
        it[0].toInt()..it[1].toInt()
    }

    fun Pair<IntRange, IntRange>.hasCompleteOverlap() : Boolean {
      return if (first.first < second.first) {
           first.last >= second.last
       } else if(first.first > second.first) {
           second.last >= first.last
      } else {
          true
      }
    }

    fun Pair<IntRange, IntRange>.hasOverlap() : Boolean = first.intersect(second).isNotEmpty()

    @Test
    fun part1Example() {
        val result = parseInput(example)
            .count { it.hasCompleteOverlap() }
        println(result)
        assertEquals(2, result)
    }

    @Test
    fun part1() {
        val result = input
            .count { it.hasCompleteOverlap() }
        println(result)
        assertEquals(448, result)
    }

    @Test
    fun part2Example() {
        val result = parseInput(example).count { it.hasOverlap() }
        println(result)
        assertEquals(4, result)
    }

    @Test
    fun part2() {
        val result = input.count { it.hasOverlap() }
        println(result)
        assertEquals(794, result)
    }
}