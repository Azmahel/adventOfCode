package de.twittgen.aoc.y2021

import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day11 {
    val input by lazy { FileUtil.readInput("2021/day11").parse() }
    val example = """5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526""".parse()

    private fun String.parse() = lines().map { line-> line.map { it.digitToInt() } }.toMap()

    private fun List<List<Int>>.toMap() = flatMapIndexed { x, it -> it.mapIndexed { y, v -> (x to y) to v } }.toMap()

    private fun Pair<Int,Int>.getAdjacents() = listOf(
        (first-1) to second,
        (first+1) to second,
        first to (second-1),
        first to (second+1),
        (first-1) to (second-1),
        (first+1) to (second-1),
        (first-1) to (second+1),
        (first+1) to (second+1),
    )

    private fun Map<Pair<Int,Int>,Int>.print() = (0..9).map { x -> (0..9).map { y -> get(x to y) }.joinToString("") }
    fun Map<Pair<Int,Int>,Int>.runStep(): MutableMap<Pair<Int, Int>, Int> {
        val flashed = mutableSetOf<Pair<Int,Int>>()
        val current = mapValues { (_,v) -> v + 1 }.toMutableMap()
        while(current.any { it.value > 9 && it.key !in flashed }) {
            val flash = current.filter { it.value >9 && it.key !in flashed}.keys
            flashed.addAll(flash)
            val adjacents = flash.flatMap { it.getAdjacents() }
            adjacents.forEach {
                if(it in current.keys) current[it] = current[it]!! + 1
            }
        }
        flashed.forEach {
            current[it] =0
        }
        return current
    }

    fun Map<Pair<Int,Int>,Int>.runSteps(i: Int, totalFlashes: Int = 0): Int
    {
        val next = runStep()
        val flashes = next.values.count { it == 0 }
        if (i==1) return  flashes + totalFlashes
        return next.runSteps(i-1, flashes +totalFlashes)
    }

    fun Map<Pair<Int,Int>,Int>.findSyncronisation(): Int {
        var steps = 0
        var current = this
        while(true) {
            if(current.values.all { it == 0 }) return steps
            current = current.runStep()
            steps ++
        }
    }



    @Test
    fun example() {
        val result = example.runSteps(100)
        assertEquals(1656, result)
    }

    @Test
    fun example2() {
        val result = example.findSyncronisation()
        assertEquals(195, result)
    }

    @Test
    fun part1() {
        val result = input.runSteps(100)
        println(result)
    }

    @Test
    fun part2() {
        val result = input.findSyncronisation()
        println(result)
    }
}

