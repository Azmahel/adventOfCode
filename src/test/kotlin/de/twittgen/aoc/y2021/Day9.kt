package de.twittgen.aoc.y2021

import de.twittgen.aoc.util.FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day9 {
    val input by lazy { FileUtil.readInput("2021/day9").parse() }
    val example = """2199943210
3987894921
9856789892
8767896789
9899965678""".parse()

    private fun String.parse() = lines().map {line -> line.map { it.digitToInt() } }.toMap()

    private fun List<List<Int>>.toMap() = flatMapIndexed { x, it -> it.mapIndexed { y, v -> (x to y) to v } }.toMap()

    private fun Pair<Int,Int>.getAdjacents() = listOf(
        (first-1) to second,
        (first+1) to second,
        first to (second-1),
        first to (second+1)
    )

    private fun findLowPoints(map : Map<Pair<Int,Int>, Int>): Map<Pair<Int, Int>, Int> {
        return map.filter { (coord , i) -> coord.getAdjacents().mapNotNull { map[it] }.all { it > i } }
    }

    private fun getBasins(map: Map<Pair<Int,Int>, Int>, lowPoints: Map<Pair<Int,Int>, Int>): List<Set<Pair<Int, Int>>> {
        val basins = lowPoints.keys.map { mutableSetOf(it) }
        while(!isComplete(map, basins)) {
            basins.map { basin ->
                basin.addAll(basin.flatMap { coord -> coord.getAdjacents().filter { (map[it]?: 9) !=9 } }.distinct())
            }
        }
        return basins
    }

    private fun List<Set<Pair<Int, Int>>>.getScore(): Int {
        return sortedByDescending { it.size }.take(3).fold(1) { i, it -> i * it.size }
    }

    private fun isComplete(
        map: Map<Pair<Int, Int>, Int>,
        basins: List<Set<Pair<Int, Int>>>
    ) = map.minus(basins.flatten().toSet()).all { (_, v) -> v == 9 }

    @Test
    fun example() {
        val result =findLowPoints(example).values.sumOf { it+1 }
        assertEquals(15, result)
    }


    @Test
    fun example2() {
        val low = findLowPoints(example)
        val basins = getBasins(example, low)
        assertEquals(1134, basins.getScore())
    }

    @Test
    fun part1() {
        val result =findLowPoints(input).values.sumOf { it+1 }
        println(result)
    }

    @Test
    fun part2() {
        val low = findLowPoints(input)
        val basins = getBasins(input, low)
        println(basins.getScore())
    }
}

