package de.twittgen.aoc.old.y2019.day5

import de.twittgen.aoc.util.FileUtil.readInput
import de.twittgen.aoc.util.toIntcodeProgram
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day5 {
    val input = readInput("2019/day5").toIntcodeProgram()

    @Test
    fun getA() {
        val computer = Day5IntCodeComputer(input)
        val result = computer.run(1)
        println(result.last())
        assertEquals(3122865,result.last())
    }

    @Test
    fun getB() {
        val computer = Day5IntCodeComputer(input)
        val result = computer.run(5)
        println(result.last())
        assertEquals(773660,result.last())
    }
}