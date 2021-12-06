package de.twittgen.aoc.y2021

import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day6 {
    val input by lazy { FileUtil.readInput("2021/day6").parse() }
    val example = """3,4,3,1,2""".parse()

    private fun String.parse() = split(",").map { it.toInt() }.groupBy { it }.mapValues { (_,v) -> v.size.toLong() }

    private fun Map<Int,Long>.advanceTime(steps: Int): Map<Int, Long> {
        val newState = mapKeys { (k,_) -> k-1 }.toMutableMap()
        if(newState[-1] != null) {
            newState[8] = newState[-1]!!
            newState[6] = (newState[6] ?: 0) + newState[-1]!!
            newState.remove(-1)
        }
        if(steps ==1) return newState
        return newState.advanceTime(steps -1)
    }

    @Test
    fun example() {
        val finalState = example.advanceTime(80)
        val result = finalState.values.sum()
        assertEquals(5934, result)
    }

    @Test
    fun example2() {
        val finalState = example.advanceTime(256)
        val result = finalState.values.sum()
        assertEquals(26984457539, result)
    }

    @Test
    fun part1() {
        val finalState = input.advanceTime(80)
        val result = finalState.values.sum()
        println(result)
    }

    @Test
    fun part2() {
        val finalState = input.advanceTime(256)
        val result = finalState.values.sum()
        println(result)
    }
}

