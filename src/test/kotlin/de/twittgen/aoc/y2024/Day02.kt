package de.twittgen.aoc.y2024

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.mapLines
import de.twittgen.aoc.util.second
import kotlin.math.abs

class Day02 : Day<List<Report>>() {
    override fun String.parse() = mapLines { it.split(" ").map { it.toInt() } }

    init {
        part1(2, 680) { it.count { it.isSafe() } }

        part2(4, 710) { it.count {  it.isSafeDampened() } }
    }

    private fun Report.isSafeDampened() = toSubReports().count { it.isSafe() } > 0

    private fun Report.toSubReports() = List(size) { i -> toMutableList().apply { removeAt(i) } }

    private fun Report.isSafe(maxDeviation: Int = 3): Boolean {
        sorted().let {
            if(it != this && it.reversed() != this ) return false
        }
        return windowed(2,1)
            .all { abs(it.first() - it.second()) <= maxDeviation && it.first() != it.second() }
    }

    override val example = """
       7 6 4 2 1
       1 2 7 8 9
       9 7 6 2 1
       1 3 2 4 5
       8 6 4 4 1
       1 3 6 7 9
    """.trimIndent()
}

private typealias Report = List<Int>