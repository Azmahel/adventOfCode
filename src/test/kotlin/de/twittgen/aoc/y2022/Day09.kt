package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.Point2D.Companion.ORIGIN
import de.twittgen.aoc.util.mapLines
import de.twittgen.aoc.util.times

class Day09 : Day<List<Instruction>>() {
    override fun String.parse() = mapLines {
        it.split(" ").let {(a,b) -> listOf(a.single()).times(b.toInt()) }
    }.flatten()

    init {
        part1(13, 5883) { it.runWithLength(2).size }
        part2(1, 2367) { it.runWithLength(10).size }
    }

    private fun List<Instruction>.runWithLength(i: Int) = run(listOf(ORIGIN).times(i))

    private tailrec fun List<Instruction>.run(rope: Rope, path: Set<Point2D> = emptySet()): Set<Point2D> {
        if (isEmpty()) return path
        val next = with(rope) { drop(1).runningFold(head.move(current)) { head, it -> it.follow(head) } }
        return drop(1).run(next, path + next.tail)
    }

    private fun Point2D.follow(head: Point2D) =
        if (head in adjacent() + this) { this } else { this + (head - this).norm() }

    private val moveMap : Map<Char, Point2D.()-> Point2D> =
        mapOf('R' to { right() }, 'L' to { left() }, 'U' to { up() }, 'D' to { down() })

    private fun Point2D.move(direction: Char) = moveMap[direction]!!()

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
private typealias Instruction = Char
private typealias Rope = List<Point2D>
private val Rope.head get() = first()
private val Rope.tail get() = last()
private val List<Instruction>.current get() = first()
