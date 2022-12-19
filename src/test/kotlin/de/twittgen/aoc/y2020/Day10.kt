package de.twittgen.aoc.y2020

import de.twittgen.aoc.Day

class Day10 : Day<List<Int>>() {
    override fun String.parse() = lines().map(String::toInt)

    init {
        part1(7*5, 1980) { adapters ->
            adapters.getAllAdapters().findSteps().run { count { it == 1} * count { it == 3} }
        }
        part2(8, 4628074479616) {
            (listOf(0) + it.getAllAdapters().findSteps()).toAdjacency().getPaths()
        }
    }

    private fun Matrix.getPaths(): Long {
        var current = this
        return (0..lastIndex).sumOf {
            current = current * this
            current[0][lastIndex]
        }
    }

    operator fun Matrix.times(b: Matrix) = (0..lastIndex).map { i -> (0..lastIndex).map { j -> (0..lastIndex).sumOf { n ->
            this[i][n] * b[n][j]
    } } }

    private fun List<Int>.toAdjacency() = (0..lastIndex).map { x -> (0..lastIndex).map { y ->
        when (y) {
            in x+1..x+3 -> if(slice((x+1)..y).sum()<=3) 1 else 0L
            else -> 0
        }
    } }


    private fun List<Int>.findSteps() =  mapIndexedNotNull { i, a -> if (i==0) null else a - this[i-1] }

    private fun List<Int>.getAllAdapters() = listOf(0) + sorted() + (maxOrNull()!! + 3)

    override val example = """
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
}
private typealias Matrix = List<List<Long>>


