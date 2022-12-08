package de.twittgen.aoc.y2019.day3

import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.FileUtil.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

private typealias  Instruction = Pair<String,Int>
class Day3 {

    val input = readInput("2019/day3").toInstructions()

    @Test
    fun part1() {
        val wire1 = Wire(input[0])
        val wire2 = Wire(input[1])

        val result = wire1.getClosestIntersectionWith(wire2, Point2D.ORIGIN).manhattanDistanceTo(Point2D.ORIGIN)

        println(result)
        assertEquals(217,result)
    }

    @Test
    fun part2() {
        val wire1 = Wire(input[0])
        val wire2 = Wire(input[1])

        val closestIntersect = wire1.getClosestIntersectionByWireLengthWith(wire2)
        val result = wire1.getDistanceTo(closestIntersect) + wire2.getDistanceTo(closestIntersect)

        println(result)
        assertEquals(3454,result)
    }

    private fun String.toInstructions(): List<List<Instruction>> {
        return filterNot { it == '\r' }
            .split("\n")
            .map {
                it.split(",")
                    .map {
                        it[0].toString() to it.substring(1).toInt(10)
                    }
            }
    }

}