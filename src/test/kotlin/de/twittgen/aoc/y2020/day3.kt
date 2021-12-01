package de.twittgen.aoc.y2020

import de.twittgen.aoc.y2019.Day12
import de.twittgen.aoc.y2019.Day12.Coordinate
import de.twittgen.aoc.y2019.shared.Point2D
import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Test

class day3 {
    val input = FileUtil.readInput("2020/day3")
    val example = """
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

    fun parseInput(s: String): Treemap {
        return s.lines().mapIndexed { x, line ->
            line.mapIndexed { y, value -> Point2D(x,y) to (value == '#') }
        }.flatten().associate { it }
    }

    @Test
    fun example() {
        val map = parseInput(example)
        val collisions = map.countCollision(1, 3)
        assert( collisions == 7)
    }

    @Test
    fun part1() {
        val map = parseInput(input)
        val collisions = map.countCollision(1, 3)
        println(collisions)
    }

    @Test
    fun part2() {
        val map = parseInput(input)
        val collisions = listOf(
            1 to 1,
            1 to 3,
            1 to 5,
            1 to 7,
            2 to 1
        ).map { (x,y) ->
            map.countCollision(x,y)
        }
        println(collisions.foldRight(1) {x,y -> x*y})
    }

    private fun Treemap.countCollision(slopeX: Int, slopeY: Int): Int {
        val maxX = keys.maxByOrNull { it.x }!!.x
        val maxY = keys.maxByOrNull { it.y }!!.y
        val positions= (0..maxX step slopeX)
            .mapIndexed { step, x ->
                get(Point2D(x,(slopeY*step) % (maxY+1)))!!
            }
        return positions.count { it }
    }


}

typealias Treemap = Map<Point2D, Boolean>