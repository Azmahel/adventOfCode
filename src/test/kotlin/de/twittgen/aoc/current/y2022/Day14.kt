package de.twittgen.aoc.current.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.current.y2022.Day14.Cave
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.rangeOf
import de.twittgen.aoc.util.second

class Day14 : Day<Cave>()  {

    override fun String.parse() = Cave(lines().flatMap { it.toRock() }.toSet())

    private fun String.toRock() =  split(" -> ")
        .map { it.split(",").run { Point2D(first().toInt(), second().toInt()) } }
        .windowed(2)
        .flatMap { (a,b) -> lineToPoints(a,b)}

    private fun lineToPoints(a: Point2D, b: Point2D) = (rangeOf(a.x, b.x)).flatMap { x -> rangeOf(a.y, b.y).map { y -> Point2D(x,y) } }

    init {
        part1(24,832) { it.simulate().size }
        part2(93, ) { it.addFloor().simulate().size }
    }



    private fun Cave.addFloor() = Cave(
        walls + lineToPoints(Point2D(500-(floor+2), floor+2) , Point2D(500+floor+2, floor +2) ) //technically it should be -inf and inf, but this fits
    )

    private tailrec fun Cave.simulate(current : Point2D = spawn) : Set<Point2D> {
        if(current.y > floor || current in blocked) return sands
        val next = current.getNextOptions().first { it !in blocked }
        return if( next != current) simulate(next) else Cave(walls, sands+next).simulate()
    }

    //positive y is down, so we go "up" on a normal grid
    private fun Point2D.getNextOptions() = listOf(up(), up().left(), up().right(), this )

    data class Cave(
        val walls: Set<Point2D>,
        val sands: Set<Point2D> = emptySet(),
        val spawn: Point2D = Point2D(500,0)
    ) {
        val blocked = walls + sands
        val floor = blocked.maxOf { it.y }
    }

    override val example = """
       498,4 -> 498,6 -> 496,6
       503,4 -> 502,4 -> 502,9 -> 494,9
    """.trimIndent()
}


