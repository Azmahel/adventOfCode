package de.twittgen.aoc.y2020

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D

class Day03 : Day<Treemap>() {

    override fun String.parse() = lines()
        .mapIndexed { x, line -> line.mapIndexed { y, value -> Point2D(x, y) to (value == '#') } }
        .flatten().associate { it }

    private val slopesToCheck = listOf(1 to 1, 1 to 3, 1 to 5, 1 to 7, 2 to 1)

    init {
        part1(7, 250) { it.countCollision(1,3) }
        part2(336, 1592662500) {
            slopesToCheck.map { (x,y) -> it.countCollision(x,y) }.foldRight(1) { x,y -> x*y }
        }
    }

    private fun Treemap.countCollision(slopeX: Int, slopeY: Int) = (0..keys.maxByOrNull { it.x }!!.x step slopeX)
        .mapIndexed { step, x -> get(Point2D(x, (slopeY * step) % (keys.maxByOrNull { it.y }!!.y + 1)))!! }
        .count { it }

    override val example = """
            ..##.......
            #...#...#..
            .#....#..#.
            ..#.#...#.#
            .#...##..#.
            ..#.##.....
            .#.#.#....#
            .#........#
            #.##...#...
            #...##....#
            .#..#...#.#
        """.trimIndent()
}

 private typealias Treemap = Map<Point2D, Boolean>