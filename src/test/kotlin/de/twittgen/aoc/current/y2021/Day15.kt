package de.twittgen.aoc.current.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.ofLength


class Day15 : Day<Map<Point2D, Int>>() {

    override fun String.parse() = lines()
        .map { line -> line.toList().map { it.digitToInt() }}
        .flatMapIndexed { y, line -> line.mapIndexed { x, i -> Point2D(x , y ) to i }  }
        .toMap()

    init {
        part1(40, 403) { dijkstra() }
        part2(315, 2840) { expand(5).dijkstra() }
    }

    private fun Map<Point2D,Int>.expand(times: Int = 5): Map<Point2D, Int> {
        val maxX = keys.maxByOrNull { it.x }!!.x + 1
        val maxY = keys.maxByOrNull { it.y }!!.y +1
        return entries.flatMap { (k, v) -> ofLength(times) { dx -> ofLength(times) { dy ->
            Point2D(k.x + (maxX*dx), (k.y + (maxY*dy))) to ((v+dx+dy)-1) % 9 + 1
        } }.flatten() }.toMap()

    }

    private fun Map<Point2D,Int>.dijkstra(): Int {
        val start = Point2D(0, 0)
        val end = Point2D(keys.maxByOrNull { it.x }!!.x, keys.maxByOrNull { it.y }!!.y)
        val dist = mutableMapOf(start to 0 )
        val distQ = mutableMapOf(start to 0)
        while(distQ.isNotEmpty()) {
            val current = distQ.minByOrNull { it.value }!!.key
            if (current == end) return distQ[end]!!
            distQ.remove(current)
            current.getAdjacent(this).forEach { neighbor ->
                val alt = dist[current]!! + get(neighbor)!!
                if (alt < (dist[neighbor] ?: Int.MAX_VALUE)) {
                    dist[neighbor] = alt
                    distQ[neighbor] = alt
                }
            }
        }
        return dist[end]!!
    }

    private fun Point2D.getAdjacent(map: Map<Point2D,Int>): Set<Point2D> = orthogonallyAdjacent().intersect(map.keys)

    override val example = """
        1163751742
        1381373672
        2136511328
        3694931569
        7463417111
        1319128137
        1359912421
        3125421639
        1293138521
        2311944581
    """.trimIndent()
}

