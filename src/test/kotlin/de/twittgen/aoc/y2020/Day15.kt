package de.twittgen.aoc.y2020

import de.twittgen.aoc.Day

class Day15 : Day<List<Int>>() {
    override val example = "3,1,2"

    override fun String.parse() = split(',').map { it.toInt() }

    init {
        part1(1836, 475) { playGame(it, 2020) }
        part2(362, 11261) { playGame(it, 30000000) }
    }

    private fun playGame(start :List<Int>, until: Int): Int {
        val oc = IntArray(until).also { start.forEachIndexed { i, n-> it[n] = i+1 } }
        var last = start.last()
        for (i in start.size until until) {
            last = oc[last].let { if(it == 0) 0 else (i - it) }.also { oc[last] = i }
        }
        return last
    }
}