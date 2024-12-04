package de.twittgen.aoc.y2024

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.*
import de.twittgen.aoc.util.Point2D.Companion.directions

class Day04 : Day<Puzzle>() {
    override fun String.parse() = mapLines(String::toCharList)

    init {
        part1(18, 2370) {it.mapCoordinates { x, y, _ ->  it.findXmasAt(x,y) }.sum() }
        part2(9, 1908) { it.mapCoordinates { x, y, _ -> it.findCrossMasAt(x,y) }.sum() }
    }

    private fun Puzzle.findXmasAt(x: Int, y: Int) : Int {
        if (this[x][y] != 'X') return 0
        val hasMASInDirection = { (dx,dy) : Point2D ->
            (1..3).map { getOrNullAt(x + (dx * it), y + (dy * it)) } == listOf('M', 'A', 'S')
        }
        return directions.map { hasMASInDirection(it) }.count { it }
    }

    private fun Puzzle.findCrossMasAt(x:Int, y:Int): Int {
        if (this[x][y] != 'A') return 0
        if (setOf(getOrNullAt(x - 1, y - 1), getOrNullAt(x + 1, y + 1)) != setOf('M', 'S')) return 0
        if (setOf(getOrNullAt(x + 1, y - 1), getOrNullAt(x - 1, y + 1)) != setOf('M', 'S')) return 0
        return 1
    }
    
    override val example = """
        MMMSXXMASM
        MSAMXMSMSA
        AMXSXMAAMM
        MSAMASMSMX
        XMASAMXAMM
        XXAMMXXAMA
        SMSMSASXSS
        SAXAMASAAA
        MAMMMXMMMM
        MXMXAXMASX
    """.trimIndent()
}
private typealias Puzzle = List<List<Char>>
