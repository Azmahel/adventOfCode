package de.twittgen.aoc.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.second

class Day2 : Day<Int, Int, List<Pair<String, Int>>>() {

    override fun String.parse() = lines().map { it.split(" ").run { first() to second().toInt() } }

    init {
        part1(150, 2102357) { move(start).run { first * second } }
        part2(900, 2101031224) { moveWithAim(start).run { first * second } }
    }

    private tailrec fun List<Pair<String, Int>>.move(point: Pair<Int, Int>) : Pair<Int, Int> {
        return if (isEmpty()) point else drop(1).move(getNext(point))
    }

    private fun List<Pair<String, Int>>.getNext(point: Pair<Int, Int>) = first().run { when (first) {
        "forward" -> point.first + second to point.second
        "down" -> point.first to point.second + second
        "up" -> point.first to point.second - second
        else -> point
    }}

    private tailrec fun List<Pair<String, Int>>.moveWithAim(point: Pair<Int, Int>, aim: Int = 0) : Pair<Int,Int> {
        return if(isEmpty()) {
            point
        } else {
            val nextAim = first().getNextAim(aim)
            drop(1).moveWithAim(first().getNextWithAim(point, nextAim), nextAim)
        }
    }

    private fun Pair<String, Int>.getNextWithAim(point: Pair<Int, Int>, nextAim: Int) = when (first) {
        "forward" -> point.first + second to point.second + (nextAim * second)
        else -> point
    }

    private fun Pair<String, Int>.getNextAim(aim: Int) = when (first) {
        "down" -> aim + second
        "up" -> aim - second
        else -> aim
    }

    val start = 0 to 0
    override val example = """
        forward 5
        down 5
        forward 8
        up 3
        down 8
        forward 2
    """.trimIndent()
}
