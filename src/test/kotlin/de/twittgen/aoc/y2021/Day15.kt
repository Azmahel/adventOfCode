package de.twittgen.aoc.y2021

import de.twittgen.aoc.y2019.shared.Point2D
import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue

class Day15 {

    val input by lazy { FileUtil.readInput("2021/day15").parse() }
    val example = """1163751742
1381373672
2136511328
3694931569
7463417111
1319128137
1359912421
3125421639
1293138521
2311944581""".parse()

    private fun String.parse() =
        lines()
            .map { line -> line.toList().map { it.digitToInt() }}
            .flatMapIndexed { y, line -> line.mapIndexed { x, i -> Point2D(x , y ) to i }  }
            .toMap()

    private fun Map<Point2D,Int>.djkstra(): Int {
        val start = Point2D(0, 0)
        val end =Point2D(keys.maxByOrNull { it.x }!!.x, keys.maxByOrNull { it.y }!!.y)
        val dist = mutableMapOf(start to 0 )
        val distQ = mutableMapOf(start to 0)
        while(distQ.isNotEmpty()) {
            val current = distQ.minByOrNull { it.value }!!.key
            if (current == end) return distQ[end]!!
            distQ.remove(current)
            current.getAdjacentOn(this).forEach { neighbor ->
                val alt = dist[current]!! + get(neighbor)!!
                if (alt < (dist[neighbor] ?: Int.MAX_VALUE)) {
                    dist[neighbor] = alt
                    distQ[neighbor] = alt
                }
            }

        }
        return dist[end]!!
    }

    private fun Map<Point2D,Int>.expand(times: Int = 5): Map<Point2D, Int> {
        val maxX = keys.maxByOrNull { it.x }!!.x + 1
        val maxY = keys.maxByOrNull { it.y }!!.y +1
        return entries.flatMap { (k, v) ->  (0 until times).flatMap { dx ->
            (0 until times).map { dy ->
                Point2D(k.x + (maxX*dx) , (k.y + (maxY*dy))) to ((v+dx+dy)-1) % 9 +1
            }
        } }.toMap()
    }

    private fun Point2D.getAdjacentOn(map: Map<Point2D,Int>): List<Point2D> =
        listOf(
            Point2D(x,y+1),
            Point2D(x,y-1),
            Point2D(x+1,y),
            Point2D(x-1,y),
        ).filter { it in map.keys }


    @Test
    fun example() {
        val result = example.djkstra()
        assertEquals(40, result)
    }

    @Test
    fun example2() {
        val result = example.expand(5).djkstra()
        assertEquals(315, result)
    }

    @Test
    fun part1() {
        val result = input.djkstra()
        println(result)
    }

    @Test
    fun part2() {
        val result = input.expand(5).djkstra()
        println(result)
    }
}

