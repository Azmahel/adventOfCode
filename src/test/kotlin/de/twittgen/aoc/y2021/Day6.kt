package de.twittgen.aoc.y2021

import de.twittgen.aoc.Day

class Day6 : Day<Long, Long, Colony>(){
    override fun String.parse() = split(",").map(String::toInt).groupBy { it }.mapValues { (_,v) -> v.size.toLong() }

    init {
        part1(5934, 350605) { advanceTime(80).values.sum() }
        part2(26984457539, 1592778185024) { advanceTime(256).values.sum() }
    }

    private tailrec fun Colony.advanceTime(steps: Int): Colony {
        return if (steps == 0) this else decreaseTimers().reproduce().advanceTime(steps - 1)
    }

    private fun Colony.reproduce() = mapValues { (i, v) -> when (i) {
        8 ->  getOrDefault(-1, 0)
        6 -> getOrDefault(6,0) + getOrDefault(-1, 0)
        else -> v
    } }.filterKeys { it != -1 }

    override val example = """3,4,3,1,2"""
}
typealias Colony = Map<Int,Long>
private fun Colony.decreaseTimers() = mapKeys { (k, _) -> k - 1 }

