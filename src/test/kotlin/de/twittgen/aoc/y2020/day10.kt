package de.twittgen.aoc.y2020

import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Test

typealias Matrix = List<List<Long>>
class day10 {
    val input by lazy { FileUtil.readInput("2020/day10") }
    val example = """
        16
        10
        15
        5
        1
        11
        7
        19
        6
        12
        4
    """.trimIndent()
    val example2 = """
        28
        33
        18
        42
        31
        14
        46
        20
        48
        47
        24
        23
        49
        45
        19
        38
        39
        11
        1
        32
        25
        35
        8
        17
        7
        9
        4
        2
        34
        10
        3
    """.trimIndent()

    fun parseInput(s: String) = s.lines().map { it.toInt() }
    @Test
    fun example() {
        val adapters = parseInput(example)
        val steps = findSteps(getAllAdapters(adapters))
        val s = steps.groupBy { it }
        assert(
            s[1]?.size == 7
        )
        assert(
            s[3]?.size == 5
        )
    }

    @Test
    fun example2() {
        val adapters = parseInput(example2)
        val steps = findSteps(getAllAdapters(adapters))
        val s = steps.groupBy { it }
        assert(
            s[1]?.size == 22
        )
        assert(
            s[3]?.size == 10
        )
    }

    @Test
    fun part1() {
        val adapters = parseInput(input)
        val steps = findSteps(getAllAdapters(adapters))
        val s = steps.groupBy { it }
        println(
            s[1]!!.size * s[3]!!.size
        )
    }

    @Test
    fun part2() {
        val adapters = getAllAdapters(parseInput(input))
        val steps = findSteps(adapters)
        val adjacency = toAdjacency(listOf(0) + steps)
        val count = adjacency.getPaths()

        println(count)
    }


    fun Matrix.getPaths(): Long {
        var current = this
        val counts = (0..lastIndex).map {
           current = current * this
            current[0][lastIndex].toLong()
        }
        return counts.sum()
    }

    operator fun Matrix.times(b: Matrix) : Matrix {
        val a = this
        return (0..a.lastIndex).map { i ->
            (0..a.lastIndex).map { j ->
                (0..a.lastIndex).map { n -> a[i][n]*b[n][j] }.sum()
            }
        }
    }
    private fun toAdjacency(steps: List<Int>): Matrix {
        return (0..steps.lastIndex).map { x ->
            (0..steps.lastIndex).map { y ->
                when (y) {
                    in x+1..x+3 -> if(steps.slice((x+1)..y).sum()<=3) 1 else 0L
                    else -> 0
                }
            }
        }
    }

    fun findSteps(adapters: List<Int>): List<Int> {
        return adapters.mapIndexedNotNull { i, a ->
            if (i==0){
                null
            } else {
                a - adapters[i-1]
            }
        }
    }

    fun getAllAdapters(base: List<Int>) = listOf(0) + base.sorted() + (base.max()!! + 3)
}


