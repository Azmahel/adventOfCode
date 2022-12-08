package de.twittgen.aoc.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.second

class Day2 : Day<Int, Int, List<Pair<String, Int>>>() {

    override val example = """
        forward 5
        down 5
        forward 8
        up 3
        down 8
        forward 2
    """.trimIndent()

    override fun String.parse() = lines().map { with(it.split(" ")) { first() to second().toInt()}}

    init {
        part1(150, 2102357) { move(start).run { first * second } }
        part2(900, 2101031224) { moveWithAim(start).run { first * second } }
    }

    val start = 0 to 0

    private fun List<Pair<String, Int>>.move(point: Pair<Int, Int>) : Pair<Int,Int> {
        val instruction = first()
        val next = when(instruction.first) {
            "forward" -> point.first +instruction.second to point.second
            "down" -> point.first to point.second + instruction.second
            "up" -> point.first to point.second - instruction.second
            else -> point
        }
        val remaining = drop(1)
        if(remaining.isEmpty()) return next
        return remaining.move(next)
    }

    private tailrec fun List<Pair<String, Int>>.moveWithAim(point: Pair<Int, Int>, aim: Int = 0) : Pair<Int,Int> {
        val nextAim = when (first().first) {
            "down" -> aim + first().second
            "up" -> aim - first().second
            else -> aim
        }
        val next = when (first().first) {
            "forward" -> point.first + first().second to point.second + (nextAim * first().second)
            else -> point
        }
        return if (drop(1).isEmpty()) next else drop(1).moveWithAim(next, nextAim)
    }

}
