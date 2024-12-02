package de.twittgen.aoc.y2023

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.*

class Day11 : Day<List<List<Char>>>() {
    override fun String.parse() = mapLines { it.toCharList() }

    init {
        part1(374, 9556712) { it.expand().apSp().sum() }
        part2(82000210, 678626199476) { it.expand(1_000_000).apSp().sum() }
    }

    private fun List<List<Char>>.expand(times: Int = 2) : List<Point2D> {
        var x = 0
        val emptyLineIndices = emptyIndices()
        val emptyColumnIndices = columns().emptyIndices()
        return this.flatMapIndexed { i, l ->
            var y = 0
            l.mapIndexedNotNull { j, c ->
                (if(c == '#') Point2D(x,y) else null).also { y +=  if(j in emptyColumnIndices) times else 1  }
            }.also {  x +=  if(i in emptyLineIndices) times else 1 }
        }
    }

    private fun List<List<Char>>.emptyIndices() =
        mapIndexed { i, l -> i to l }.filter { (_, it) -> it.all { it == '.' } }.map { it.first }.toSet()

    private fun List<Point2D>.apSp() = flatMapIndexed { i, p -> drop(i + 1).map { p.manhattanDistance(it).toLong() } }

    override val example = """
        ...#......
        .......#..
        #.........
        ..........
        ......#...
        .#........
        .........#
        ..........
        .......#..
        #...#.....
    """.trimIndent()
}