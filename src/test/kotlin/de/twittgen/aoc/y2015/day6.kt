package de.twittgen.aoc.y2015

import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Test

class day6 {
    val input = FileUtil.readInput("2015/day6")

    @Test
    fun part1() {
        val instructions = parseInput(input)
        val grid = perform(instructions)
        println(grid.count())
    }

    @Test
    fun part2() {
        val instructions = parseInput(input)
        val grid = performV2(instructions)
        println(grid.values.sum())
    }

    private fun perform(instructions: List<Triple<String, IntRange, IntRange>>): Set<Pair<Int,Int>> {
        val turnedOn= mutableSetOf<Pair<Int,Int>>()
        for(i in instructions) {
            val affected = i.second.flatMap { x -> i.third.map { y -> x to y } }
            when(i.first) {
                "turn on" -> {
                    turnedOn.addAll(affected)
                }
                "turn off" -> {
                    turnedOn.removeAll(affected)
                }
                "toggle" -> affected.map {
                    if( it in turnedOn) turnedOn.remove(it) else turnedOn.add(it)
                }
            }
        }
        return turnedOn
    }

    private fun performV2(instructions: List<Triple<String, IntRange, IntRange>>): Map<Pair<Int, Int>, Int> {
        val turnedOn= mutableMapOf<Pair<Int,Int>,Int>()
        for(i in instructions) {
            val affected = i.second.flatMap { x -> i.third.map { y -> x to y } }
            when(i.first) {
                "turn on" -> {
                    affected.forEach {
                        turnedOn[it]?.apply {
                            turnedOn[it] = this +1
                        } ?: run { turnedOn[it] = 1 }
                    }
                }
                "turn off" -> {
                    affected.forEach {
                        turnedOn[it]?.apply {
                            turnedOn[it] = listOf(this -1,0).maxOrNull()!!
                        }
                    }
                }
                "toggle" ->  affected.forEach {
                    turnedOn[it]?.apply {
                        turnedOn[it] = this +2
                    } ?: run { turnedOn[it] = 2 }
                }
            }
        }
        return turnedOn
    }

    val instructionPattern = Regex("(.*) (\\d+),(\\d+) through (\\d+),(\\d+)")
    private fun parseInput(s: String): List<Triple<String, IntRange, IntRange>> {
        return s.lines().map {
            val (instruction, x1,y1,x2,y2) =instructionPattern.matchEntire(it)!!.destructured
            Triple(instruction, x1.toInt()..x2.toInt(), y1.toInt()..y2.toInt())
        }
    }
}