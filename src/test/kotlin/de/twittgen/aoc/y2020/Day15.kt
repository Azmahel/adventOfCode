package de.twittgen.aoc.y2020

import de.twittgen.aoc.Day
import de.twittgen.aoc.Day.TestType.SLOW

class Day15 : Day<List<Long>>() {
    override val example = "3,1,2"

    override fun String.parse() = split(',').map { it.toLong() }

    init {
        part1(1836, 475) { playGame(it, 2020) }
        part2(362, 11261) { playGame(it, 30000000) }
    }

    private val loop = Regex(".*(.+)\\1\\1")
    private fun playGame(start :List<Long>, until: Long ): Long {
        val occurrences = LinkedHashMap<Long, Long>(minOf(3_500_000, until).toInt()).apply {
            putAll(start.mapIndexed { i, it -> it to i.toLong()+1 })
        }
        var last = start.last().toLong()
        for (i in (start.size.toLong()) until until) {
            last = (occurrences[last]?.let { i- it } ?: 0).also {  occurrences[last] = i }
        }
        return last
    }
}