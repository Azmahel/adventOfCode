package de.twittgen.aoc.y2024

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.mapCoordinates

class Day10: Day<HeightMap>() {
    override fun String.parse() = mapCoordinates { x, y, c -> if(c != '.') Point2D(x,y) to c.digitToInt() else null }.toMap()

    init {
        part1(36, 694) { it.trails().distinctBy { it.first() to it.last() }.size }
        part2(81, 1497) { it.trails().groupBy { it.first() }.entries.sumOf { (_,v) -> v.size } }
    }

    private fun HeightMap.trails() = filterValues { it == 0 }.keys.flatMap { p -> followTrailAt(p) }

    private fun HeightMap.followTrailAt(start: Point2D) : List<List<Point2D>> =
        if (get(start)!! == 9) listOf(listOf(start))
        else start.orthogonallyAdjacent()
            .filter { contains(it) && get(it)!! == get(start)!! + 1 }
            .flatMap { followTrailAt(it).map { listOf(start) + it } }

    override val example = """
        89010123
        78121874
        87430965
        96549874
        45678903
        32019012
        01329801
        10456732
    """.trimIndent()
}
private typealias HeightMap = Map<Point2D, Int>