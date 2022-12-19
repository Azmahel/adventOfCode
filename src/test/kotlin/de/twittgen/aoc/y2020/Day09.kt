package de.twittgen.aoc.y2020

import de.twittgen.aoc.Day
import de.twittgen.aoc.Day.TestState.EXAMPLE
import java.lang.IllegalStateException

class Day09: Day<Cypher>() {
    override fun  String.parse() = lines().map(String::toLong)

    init {
        part1(127, 756008079) { it.findFirstInvalid(preamble()) }
        part2(62, 93727241) { it.findWeaknessV2(it.findFirstInvalid(preamble())) }
    }

    private fun preamble() = if ( testState == EXAMPLE) 5 else 25

    private fun Cypher.findWeaknessV2(key: Long): Long {
        val items = mutableListOf<Long>()
        forEach {
            items += it
            while(items.sum() > key ) items.removeAt(0)
            if(items.sum() == key) return items.maxOrNull()!! + items.minOrNull()!!
        }
        throw IllegalStateException()
    }

    private fun Cypher.findFirstInvalid(preambleLength: Int = 25): Long {
        val validNumbers = take(preambleLength).toMutableList()
        drop(preambleLength).forEach {
            if(!it.isSumOfTwoElementsIn(validNumbers.takeLast(preambleLength)) ) return it
            validNumbers += it
        }
        throw IllegalStateException()
    }

    private infix fun Long.isSumOfTwoElementsIn(list: List<Long>): Boolean  = list.any { (this - it) in (list - it) }

    override val example = """
        35
        20
        15
        25
        47
        40
        62
        55
        65
        95
        102
        117
        150
        182
        127
        219
        299
        277
        309
        576
    """.trimIndent()
}

private typealias Cypher = List<Long>

