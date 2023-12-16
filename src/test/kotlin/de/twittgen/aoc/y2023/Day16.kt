package de.twittgen.aoc.y2023

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.Point2D.Direction
import de.twittgen.aoc.util.Point2D.Direction.*
import de.twittgen.aoc.util.mapCoordinates

class Day16 : Day<Map<Point2D, Day16.Mirror>>() {
    override fun String.parse() = mapCoordinates { y, x, c ->
        Point2D(x,y) to when(c) {
            '/' -> RMirror
            '\\' -> LMirror
            '|' -> VSplitter
            '-' -> HSplitter
            '.' -> Empty
            else -> throw IllegalArgumentException()
        }
    }.toMap()

    init {
        part1(46, 7034) { it.energize() }
        part2(51, 7759) { map ->  map.getEdgePoints().maxOf { map.energize(it) }}
    }

    private fun Map<Point2D, *>.getEdgePoints() = getBoundaries().let { (rX, rY) ->
        (rX.flatMap { x -> listOf(Point2D(x, rY.first) to UP, Point2D(x, rY.last) to DOWN,)} +
                rY.flatMap { y -> listOf(Point2D(rX.first, y) to RIGHT, Point2D(rX.last, y) to LEFT,)}).toSet()
    }

    private fun Map<Point2D,Mirror>.energize( start : Pair<Point2D, Direction> = Point2D.ORIGIN to RIGHT): Int {
        val energized = mutableSetOf(start)
        var current = setOf(start)
        val (rX, rY) = getBoundaries()
        while (current.isNotEmpty()) {
            current = current
                .flatMap { (p,d)  -> get(p)!!.moveThrough(d).map { nd -> nd.next(p) to nd } }
                .filter { it !in energized }
                .filter {(p,_) -> p.x in rX && p.y in rY }
                .toSet()
            energized += current
        }
        return energized.map { it.first }.toSet().size
    }

    private fun Map<Point2D, *>.getBoundaries() =
        minOf { it.key.x }..maxOf { it.key.x } to minOf { it.key.y }.. maxOf { it.key.y }

     sealed class Mirror { abstract fun moveThrough(d : Direction) : Set<Direction> }
    private object LMirror: Mirror() {
        private val map = mapOf(LEFT to DOWN, RIGHT to UP, DOWN to LEFT, UP to RIGHT)
        override fun moveThrough(d: Direction) = setOf(map[d]!!)
    }
    private object RMirror: Mirror() {
        private val map = mapOf(LEFT to UP, RIGHT to DOWN, DOWN to RIGHT, UP to LEFT)
        override fun moveThrough(d: Direction) = setOf(map[d]!!)
    }

    private object HSplitter : Mirror() {
        override fun moveThrough(d: Direction) = if (d == LEFT || d == RIGHT) setOf(d) else setOf(LEFT, RIGHT)
    }
    private object VSplitter : Mirror() {
        override fun moveThrough(d: Direction) = if (d == UP || d == DOWN) setOf(d) else setOf(UP, DOWN)
    }
    private object Empty: Mirror() { override fun moveThrough(d: Direction) = setOf(d) }

    override val example = """
        .|...\....
        |.-.\.....
        .....|-...
        ........|.
        ..........
        .........\
        ..../.\\..
        .-.-/..|..
        .|....-|.\
        ..//.|....
    """.trimIndent()
}