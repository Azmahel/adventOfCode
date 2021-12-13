package de.twittgen.aoc.y2021

import de.twittgen.aoc.y2019.shared.util.FileUtil
import de.twittgen.aoc.y2019.shared.util.second
import de.twittgen.aoc.y2021.Day13.FoldDirection.X
import de.twittgen.aoc.y2021.Day13.FoldDirection.Y
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day13 {
    val input by lazy { FileUtil.readInput("2021/day13").parse() }
    val example = """6,10
0,14
9,10
0,3
10,4
4,11
6,0
6,12
4,1
0,13
10,12
3,4
3,0
8,4
1,10
2,14
8,10
9,0

fold along y=7
fold along x=5""".parse()

    private fun String.parse(): Pair<Set<Pair<Int, Int>>, List<Fold>> {
        val lines = lines()
        val coordinates = lines
            .takeWhile { it.isNotEmpty() }
            .map { line ->
                line.split(",").let { it.first().toInt() to it.second().toInt() }
            }.toSet()
        val folds = lines
            .takeLastWhile { it.isNotEmpty() }
            .map { line ->
                line.split(" ").last().split("=").let {
                    Fold(FoldDirection.from(it.first()),it.second().toInt())
                }
            }
        return coordinates to folds
    }
    enum class FoldDirection {
        X,
        Y;
        companion object {
            fun from(s: String) = if(s == "x") X else Y
        }
    }
    data class Fold(val direction: FoldDirection, val at: Int)

    private fun Set<Pair<Int,Int>>.foldAt(fold: Fold) =map {
        when (fold.direction) {
            X -> it.first.foldAt(fold.at) to it.second
            Y ->  it.first  to it.second.foldAt(fold.at)
        }
    }.toSet()

    private fun Set<Pair<Int, Int>>.toPaper() = (0..maxByOrNull { it.first }!!.first).joinToString("\n") { x ->
        (0..maxByOrNull { it.second }!!.second).joinToString("") { y ->
            if (x to y in this) "#" else "."
        }
    }

    private fun Int.foldAt( other: Int) = if(this <= other) this else other - (this -other)

    @Test
    fun example() {
        val (coordinates, folds) = example
        val result = coordinates.foldAt(folds.first())
        assertEquals(17, result.size)
    }

    @Test
    fun example2() {
        val (coordinates, folds) = example
        val result = folds.fold(coordinates) { c, f -> c.foldAt(f) }.toPaper()
        val expected = """#####
#...#
#...#
#...#
#####"""
        assertEquals(expected, result)
    }

    @Test
    fun part1() {
        val (coordinates, folds) = input
        val result = coordinates.foldAt(folds.first())
        println(result.size)
    }

    @Test
    fun part2() {
        val (coordinates, folds) = input
        val result = folds.fold(coordinates) { c, f -> c.foldAt(f) }.toPaper()
        println(result)
    }
}

