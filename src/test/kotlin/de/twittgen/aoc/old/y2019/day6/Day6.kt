package de.twittgen.aoc.old.y2019.day6

import de.twittgen.aoc.util.FileUtil.readInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day6 {
    private val input = readInput("2019/day6")

    @Test
    fun part1() {
        val map = OrbitMap.fromString(input)

        val result = map.getIndirectOrbitCount()

        println(result)
        assertEquals(227612, result)
    }

    @Test
    fun getB() {
        val map = OrbitMap.fromString(input)

        // -1 because the object YOU are orbiting is part of the path
        val result = map.getPath("YOU", "SAN").size -1

        println(result)
        assertEquals(454, result)
        }
}



