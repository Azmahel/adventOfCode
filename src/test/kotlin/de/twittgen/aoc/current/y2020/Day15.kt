package de.twittgen.aoc.current.y2020

import de.twittgen.aoc.current.Day

class Day15 : Day<List<Long>>() {
    override val example = "3,1,2"

    override fun String.parse() = split(',').map { it.toLong() }

    init {
        part1(1836, 475) { playGame(it, 2020) }
        part2(362, 11261) {playGame(it, 30000000)}
    }

    private fun playGame(start :List<Long>, until: Long ): Long {
        val occurences = start.mapIndexed { i, it -> it to listOf(i.toLong()) }.toMap().toMutableMap()
        var last = start.last().toLong()
        for (i in (start.lastIndex.toLong()) until until-1) {
            last = occurences[last]?.let { it.dropLast(1).lastOrNull()?.let{ i- it } ?:  0 } ?: 0
            occurences[last] = occurences[last]?.lastOrNull()?.let { listOf(it,i+1) } ?: listOf(i+1)
        }
        return last
    }
}