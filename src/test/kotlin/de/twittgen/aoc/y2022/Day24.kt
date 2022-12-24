package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.Point2D.Direction
import de.twittgen.aoc.util.Point2D.Direction.*
import de.twittgen.aoc.y2022.Day24.Blizzard

class Day24 : Day<Pair<Point2D, Set<Blizzard>>>() {
    override fun String.parse() =lines().run {
        val blizzards = drop(1).dropLast(1).flatMapIndexed { y, l -> l.mapIndexedNotNull { x, c ->
            Point2D(x - 1, -y).let { p -> when (c) {
                '<' -> Blizzard(p, LEFT)
                '>' -> Blizzard(p, RIGHT)
                '^' -> Blizzard(p, UP)
                'v' -> Blizzard(p, DOWN)
                else -> null
            } }
        } }.toSet()
        Point2D(last().indexOfFirst { it == '.' } - 1, -(lastIndex - 1)) to blizzards
    }

    init {
        part1(18, 260) {(to, b ) -> start.findWayThrough(to,b).first }
        part2(54, 747) {(to, b) -> start.findWayThrough(to,b).let { (t, b1) ->
            t + to.findWayThrough(start,b1).let { (t2, b2) ->
                t2 + start.findWayThrough(to, b2).first
            } }}
    }

    private val start = Point2D(0, 1)
    private fun Set<Blizzard>.points() = map { it.position }.toSet()
    private fun Point2D.findWayThrough(to: Point2D, blizzards: Set<Blizzard>): Pair<Int, Set<Blizzard>> {
        val current = mutableSetOf(this)
        var currentBlizzards = blizzards
        var currentBlizzardPoints = blizzards.points()
        var time = 0
        val (xR, yR) = listOf(minOf(to.x,x)..maxOf(to.x, x), (minOf(to.y,y)+1) until maxOf(to.y,y))
        fun MutableSet<Point2D>.removeOnWalls() =
            removeIf { (it.x !in xR || it.y !in yR) && it != this@findWayThrough && it != to }
        fun MutableSet<Point2D>.removeOnBlizzard() { removeIf { it in currentBlizzardPoints } }
        fun moveBlizzards() {
            currentBlizzards = currentBlizzards.map { it.next().run { when {
                (position.y < yR.first) ->  copy(position = Point2D(position.x, yR.last))
                (position.y > yR.last) ->  copy(position = Point2D(position.x, yR.first))
                (position.x < xR.first) ->  copy(position = Point2D(xR.last, position.y))
                (position.x > xR.last) ->  copy(position = Point2D(xR.first, position.y))
                else -> this
            } } }.toSet()
            currentBlizzardPoints = currentBlizzards.points()
        }
        while (to !in current) {
            current.addAll(current.flatMap { it.orthogonallyAdjacent() })
            moveBlizzards()
            current.removeOnWalls()
            current.removeOnBlizzard()
            time++
        }
        return time to currentBlizzards
    }

    data class Blizzard(val position: Point2D, val direction: Direction) {
        fun next() = copy(position = direction.next(position))
    }

    override val example = """
        #.######
        #>>.<^<#
        #.<..<<#
        #>v.><>#
        #<^v^^>#
        ######.#
    """.trimIndent()
}