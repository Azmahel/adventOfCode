package de.twittgen.aoc.y2024

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.toGrid

class Day12 : Day<List<Plot>>() {
    override fun String.parse() = toGrid { p, c -> c to p }.toSet().toPlots().flatMap { it.value }

    init {
        part1(1930, 1461806) { it.sumOf { it.fencingCost() } }
        part2(1206, 887932) { it.sumOf { it.bulkFencing() } }
    }

    private fun Plot.bulkFencing() = size * toEdges().size
    private fun Plot.toEdges() =
        expand().let { plot -> plot.filter { it.adjacent().filter { it in plot }.size in setOf(7, 3, 4) } }

    private fun Plot.expand() = flatMap { (x, y) ->
        listOf(
            Point2D(x * 2, y * 2), Point2D(x * 2 + 1, y * 2), Point2D(x * 2 + 1, y * 2 + 1), Point2D(x * 2, y * 2 + 1),
        )
    }
    private fun Plot.fencingCost() = size * sumOf { it.orthogonallyAdjacent().filter { !contains(it) }.size }
    private fun Garden.toPlots(): Plots =
        groupBy { it.first }.mapValues { (_, v) -> v.map { it.second }.toSet().toPlots() }

    private fun Set<Point2D>.toPlots(): Set<Plot> =
        fold(emptySet()) { c, p -> c + setOf((if (c.all { p !in it }) dfs(setOf(p)) else emptySet())) }


    private fun Set<Point2D>.dfs(current: Plot): Plot = current.flatMap { it.orthogonallyAdjacent() }
        .filter { it in this && it !in current }
        .let { if (it.isEmpty()) current else dfs(current + it) }

    override val example = """
        RRRRIICCFF
        RRRRIICCCF
        VVRRRCCFFF
        VVRCCCJFFF
        VVVVCJJCFE
        VVIVCCJJEE
        VVIIICJJEE
        MIIIIIJJEE
        MIIISIJEEE
        MMMISSJEEE
    """.trimIndent()
}
private typealias Garden = Set<Pair<Char, Point2D>>
private typealias Plots = Map<Char, Set<Plot>>
private typealias Plot = Set<Point2D>