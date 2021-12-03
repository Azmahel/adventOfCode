package de.twittgen.aoc.y2021

import de.twittgen.aoc.y2019.shared.util.FileUtil
import de.twittgen.aoc.y2019.shared.util.second
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class Day2 {
    val input = FileUtil.readInput("2021/day2").lines().map { with(it.split(" ")) { first() to second().toInt()}}
    val example = """forward 5
down 5
forward 8
up 3
down 8
forward 2""".lines().map { with(it.split(" ")) { first() to second().toInt()}}

    val start = 0 to 0

    private fun Pair<Int,Int>.move(instructions : List<Pair<String,Int>>) : Pair<Int,Int> {
        val instruction = instructions.first()
        val next = when(instruction.first) {
            "forward" -> first +instruction.second to second
            "down" -> first to second + instruction.second
            "up" -> first to second - instruction.second
            else -> this
        }
        val remaining = instructions.drop(1)
        if(remaining.isEmpty()) return next
        return next.move(remaining)
    }

    private fun Pair<Int,Int>.moveWithAim(instructions : List<Pair<String,Int>>, aim: Int = 0) : Pair<Int,Int> {
        val instruction = instructions.first()

        val nextAim = when(instruction.first) {
            "down" -> aim + instruction.second
            "up" -> aim - instruction.second
            else -> aim
        }

        val next = when(instruction.first) {
            "forward" -> first +instruction.second to second + (nextAim * instruction.second)
            else -> this
        }
        val remaining = instructions.drop(1)
        if(remaining.isEmpty()) return next
        return next.moveWithAim(remaining, nextAim)
    }

    @Test
    fun example() {
        val result = start.move(example)
        assertEquals(150, result.first * result.second)
    }

    @Test
    fun example2() {
        val result = start.moveWithAim(example)
        assertEquals(900, result.first * result.second)
    }

    @Test
    fun part1() {
        val result = start.move(input)
        println(result.first * result.second)
    }

    @Test
    fun part2() {
        val result = start.moveWithAim(input)
        println(result.first * result.second)
    }
}
