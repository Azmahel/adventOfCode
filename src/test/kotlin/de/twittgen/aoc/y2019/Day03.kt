package de.twittgen.aoc.y2019

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D
import java.lang.IllegalArgumentException

class Day03 : Day<List<Day03.Wire>>() {
    override fun String.parse() =
        lines().map { Wire(it.split(",").map { it[0] to it.drop(1).toInt() }) }

    init {
        part1(159, 217) { (w1, w2) -> w1.getClosestIntersection(w2).manhattanDistance() }
        part2(610, 3454) { (w1, w2) -> w1.getClosestIntersectionByLength(w2).let {
            w1.getDistanceTo(it) + w2.getDistanceTo(it)
        } }
    }

    class Wire(instructions: List<Instruction>) {
        private val path: List<Point2D> = instructions.generatePath()

        fun getDistanceTo(point: Point2D) = path.indexOf(point)

        fun getClosestIntersectionByLength(other: Wire) =
            getIntersections(other).minByOrNull { getDistanceTo(it) + other.getDistanceTo(it) }!!

        fun getClosestIntersection(other: Wire, to: Point2D = Point2D.ORIGIN) =
            getIntersections(other).minByOrNull { it.manhattanDistance(to) }!!

        private fun getIntersections(other: Wire): List<Point2D> =
            path.intersect(other.path.toSet()).filterNot { it == Point2D.ORIGIN }

        private fun List<Instruction>.generatePath() = fold(listOf(Point2D.ORIGIN)) { current, (operation, count) ->
            current + current.last().let { last -> (1..count).map {when (operation) {
                'U' -> last.up(it)
                'D' -> last.down(it)
                'R' -> last.left(it)
                'L' -> last.right(it)
                else -> throw IllegalArgumentException("Unknown direction $operation")
            }}}
        }
    }

    override val example = """
        R75,D30,R83,U83,L12,D49,R71,U7,L72
        U62,R66,U55,R34,D71,R55,D58,R83
    """.trimIndent()
}
private typealias  Instruction = Pair<Char,Int>
