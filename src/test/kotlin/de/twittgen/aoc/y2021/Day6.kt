package de.twittgen.aoc.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day6 : Day<Long, Long, Map<Int, Long>>(){
    override fun String.parse() = split(",").map(String::toInt).groupBy { it }.mapValues { (_,v) -> v.size.toLong() }

    init {
        part1(5934, 350605) { advanceTime(80).values.sum() }
        part2(26984457539, 1592778185024) { advanceTime(256).values.sum() }
    }

    private tailrec fun Map<Int,Long>.advanceTime(steps: Int): Map<Int, Long> {
        return if(steps == 0) {
            this
        } else {
            mapKeys { (k,_) -> k-1 }.toMutableMap().apply { reproduce() }.advanceTime(steps -1)
        }
    }

    private fun MutableMap<Int, Long>.reproduce() {
        set(8, getOrDefault(-1, 0))
        set(6, getOrDefault(6,0) + getOrDefault(-1, 0))
        remove(-1)
    }

    override val example = """3,4,3,1,2"""
}

