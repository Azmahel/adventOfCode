package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.y2022.Day14.Cave
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.rangeOf

class Day14 : Day<Cave>()  {

    override fun String.parse() = Cave(lines().flatMap { it.toRock() }.toSet())

    private fun String.toRock() =  split(" -> ")
        .map { it.split(",").let { (x,y) ->  Point2D(x.toInt(), y.toInt()) } }
        .windowed(2)
        .flatMap { (a,b) -> lineToPoints(a,b)}

    private fun lineToPoints(a: Point2D, b: Point2D) = (rangeOf(a.x, b.x)).flatMap { x -> rangeOf(a.y, b.y).map { y ->
        Point2D(x,y)
    } }

    init {
        part1(24,832) { it.simulate().size }
        part2(93, 27601) { it.addFloor().simulate().size }
    }

    private fun Cave.addFloor(i: Int = 2) = Cave(
        walls + lineToPoints(Point2D(spawn.x-(floor+i), floor+i) , Point2D(spawn.x+floor+i, floor +i) )
    )

    private fun Cave.simulate() : Set<Point2D> {
        var current = spawn
        val sands = mutableSetOf<Point2D>()
        while(current.y <= floor && current !in sands) {
            val next = current.getNextOptions().first { it !in walls  && it !in sands }
            current = if (next == current) spawn.also {  sands.add(next) } else next
        }
        return sands
    }

    // positive y is down, so we go "up" on a normal grid
    private fun Point2D.getNextOptions() = listOf(up(), up().left(), up().right(), this )

    data class Cave(val walls: Set<Point2D>, val spawn: Point2D = Point2D(500,0)) {
        val floor = walls.maxOf { it.y }
    }

    override val example = """
       498,4 -> 498,6 -> 496,6
       503,4 -> 502,4 -> 502,9 -> 494,9
    """.trimIndent()
}


