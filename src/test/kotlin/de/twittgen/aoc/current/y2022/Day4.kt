package de.twittgen.aoc.current.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.containsAll

 private typealias Assignment = Pair<IntRange, IntRange>

class Day4 : Day<List<Assignment>> () {
    override fun String.parse() = lines().map { it.mapLine() }
    private fun String.mapLine() = split(',').map { it.parseRange() }.let { it[0] to it[1] }
    private fun String.parseRange() = split('-').let { it[0].toInt()..it[1].toInt() }

    init {
        part1(2, 448) { count { it.hasCompleteOverlap() } }
        part2(4, 794) { count { it.hasOverlap() } }
    }

    private fun Assignment.hasCompleteOverlap() = first containsAll second || second containsAll first

    private fun Pair<IntRange, IntRange>.hasOverlap() : Boolean = first.intersect(second).isNotEmpty()

    override val example = """
        2-4,6-8
        2-3,4-5
        5-7,7-9
        2-8,3-7
        6-6,4-6
        2-6,4-8
    """.trimIndent()
}