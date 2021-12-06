package de.twittgen.aoc.y2021

import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class Day1 {
    val input  by lazy { FileUtil.readInput("2021/day1").lines().map { it.toInt() } }
    val example = """199
200
208
210
200
207
240
269
260
263""".lines().map { it.toInt() }

    private fun getDepthIncreased(points: List<Int>) = points.drop(1).mapIndexed { i , it ->
        it > points[i]
    }
    private fun List<Int>.to3PMeassurement() =
        dropLast(2).mapIndexed { i, it ->
            it+this[i+1]+this[i+2]
        }

    @Test
    fun example() {
        val increases = getDepthIncreased(example).count { it }
        assertEquals(7, increases)
    }

    @Test
    fun example2() {
        val increases = getDepthIncreased(example.to3PMeassurement()).count { it }
        assertEquals(5, increases)
    }

    @Test
    fun part1() {
        val increases = getDepthIncreased(input).count { it }
       println(increases)
    }

    @Test
    fun part2() {
        val increases = getDepthIncreased(input.to3PMeassurement()).count { it }
         println(increases)
    }
}
