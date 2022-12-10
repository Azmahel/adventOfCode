package de.twittgen.aoc.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.Point2D.Companion.ORIGIN
import de.twittgen.aoc.util.second

class Day2 : Day<Int, Int, List<Pair<String, Int>>>() {

    override fun String.parse() = lines().map { it.split(" ").run { first() to second().toInt() } }

    init {
        part1(150, 2102357) { move().run { x * y } }
        part2(900, 2101031224) { moveWithAim().run { x * y } }
    }

    private tailrec fun List<Pair<String, Int>>.move(point: Point2D= ORIGIN) : Point2D {
        return if (isEmpty()) point else drop(1).move(getNext(point))
    }

    private fun List<Pair<String, Int>>.getNext(point: Point2D) = first().let { (d , v) -> when (d) {
        "forward" -> Point2D(point.x + v , point.y)
        "down" -> Point2D(point.x, point.y + v)
        "up" -> Point2D(point.x, point.y - v)
        else -> point
    }}

    private tailrec fun List<Pair<String, Int>>.moveWithAim(point: Point2D= ORIGIN, aim: Int = 0) : Point2D {
        return if(isEmpty()) {
            point
        } else {
            val nextAim = first().getNextAim(aim)
            drop(1).moveWithAim(first().getNextWithAim(point, nextAim), nextAim)
        }
    }

    private fun Pair<String, Int>.getNextWithAim(point: Point2D, nextAim: Int) = when (first) {
        "forward" -> Point2D(point.x + second, point.y + (nextAim * second))
        else -> point
    }

    private fun Pair<String, Int>.getNextAim(aim: Int) = when (first) {
        "down" -> aim + second
        "up" -> aim - second
        else -> aim
    }

    override val example = """
        forward 5
        down 5
        forward 8
        up 3
        down 8
        forward 2
    """.trimIndent()
}
