package de.twittgen.aoc.y2019.day2

import de.twittgen.aoc.y2019.shared.util.FileUtil
import de.twittgen.aoc.y2019.shared.util.toIntcodeProgram
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.IllegalStateException

class Day2 {
    val input = FileUtil.readInput("day2").toIntcodeProgram()

    @Test
    fun part1() {
        val computer = IntCodeComputer(input)
        val result = computer.run(12,2)
        println(result)
        assertEquals(3765464, result)
    }


    @Test
    fun part2() {
        val computer = IntCodeComputer(input)

        (0..99).forEach {noun ->
            (0..99).forEach { verb ->
                if(computer.run(noun,verb) == 19690720) {
                    val result =  100* noun + verb
                    println(100* noun + verb)
                    assertEquals(7610, result)
                    return
                }
            }
        }
        throw IllegalStateException("no Solution found")
    }
}