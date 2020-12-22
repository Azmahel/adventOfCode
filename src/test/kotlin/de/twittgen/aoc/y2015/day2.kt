package de.twittgen.aoc.y2015

import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Test

class day2 {
    val input = FileUtil.readInput("2015/day2")
    val example = """
        2x3x4
        1x1x10
    """.trimIndent()

    fun parseInput(s: String): List<List<Int>> {
        return s.lines().map{it.split("x").map { it.toInt() }}
    }

    @Test
    fun example() {
        val boxes  = parseInput(example).map{ getSides(it) }
        val total = boxes.map { sides -> sides.map { it }.sum() + sides.min()!!}
        assert(total == listOf(58,43))
    }

    @Test
    fun part1() {
        val boxes  = parseInput(input).map{ getSides(it) }
        val total = boxes.map { sides -> sides.map { it }.sum() + sides.min()!!}
        println(total.sum())
    }

    @Test
    fun part2() {
        val boxes  = parseInput(input)
        val rounds = boxes.map { getWrapLength(it) }
        val bows = boxes.map { getBowLength(it) }
        println(rounds.sum() + bows.sum())
    }

    private fun getBowLength(box: List<Int>) = box.reduce { x,y -> x*y}

    private fun getWrapLength(box: List<Int>) =  box.sorted().dropLast(1).sum() *2

    private fun getSides(dimensions: List<Int>): List<Int> {
        return dimensions.mapIndexed { i, dim1 ->
            dimensions.mapIndexedNotNull { j, dim2 -> if (i!=j) dim1*dim2 else null }
        }.flatten()
    }
}