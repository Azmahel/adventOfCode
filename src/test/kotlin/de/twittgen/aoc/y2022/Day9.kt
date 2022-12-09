package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.Point2D.Companion.ORIGIN
import de.twittgen.aoc.util.second
import de.twittgen.aoc.util.times
import kotlin.math.sign

typealias Instruction = Pair<Char,Int>
class Day9 : Day<Int, Int, List<Instruction>>() {
    override fun String.parse() = lines().map { it.split(" ").run { first().single() to second().toInt() } }

    init {
        part1(13, 5883) { run().size }
        part2(1, 2367) { runWithLength(10).size }
    }

    private fun List<Instruction>.runWithLength(i: Int) = run(listOf(ORIGIN).times(i))

    private tailrec fun List<Instruction>.run(
        rope: List<Point2D> = listOf(ORIGIN, ORIGIN),
        path: Set<Point2D> = emptySet()
    ): Set<Point2D> {
        if (isEmpty()) return path
        val (direction, amount) = first()
        val newRope = with(rope) { drop(1).runningFold(first().move1(direction)) { before, it -> it.follow(before) } }
        val remaining = if(amount == 1) drop(1) else listOf(Instruction(direction, amount-1)) + drop(1)
        return remaining.run(newRope, path + newRope.last())
    }

    private fun Point2D.follow(newH: Point2D) =
        if (newH in adjacent() + this) { this } else { Point2D(x + (newH.x - x).sign, y + (newH.y - y).sign) }

    private val moveMap : Map<Char, Point2D.()-> Point2D> =
        mapOf('R' to Point2D::right, 'L' to Point2D::left, 'U' to Point2D::up, 'D' to Point2D::down)
    private fun Point2D.move1(direction: Char) = moveMap[direction]!!()

    override val example = """
        R 4
        U 4
        L 3
        D 1
        R 4
        D 1
        L 5
        R 2
    """.trimIndent()
}