package de.twittgen.aoc.y2020

import de.twittgen.aoc.y2019.shared.util.FileUtil
import de.twittgen.aoc.y2019.shared.util.second
import org.junit.jupiter.api.Test

typealias Cypher = List<Long>
class day9 {

    val input = FileUtil.readInput("2020/day9")
    val example = """
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

    fun parseInput(s: String) = s.lines().map(String::toLong)

    @Test
    fun example() {
        val cypher = parseInput(example)
        val result = cypher.findFirstInvalid(5)
        assert(
            result == 127L
        )
    }

    @Test
    fun part1() {
        val cypher = parseInput(input)
        val result = cypher.findFirstInvalid(25)
        println(result)
    }

    @Test
    fun part2Example() {
        val cypher = parseInput(example)
        val key = cypher.findFirstInvalid(5)
        val weakness = cypher.findWeakness(key!!)
        assert(
            weakness == 62L
        )
    }

    @Test
    fun part2() {
        val cypher = parseInput(input)
        val key = cypher.findFirstInvalid(25)
        val weakness = cypher.findWeakness(key!!)
        println(weakness)
    }

    fun Cypher.findWeakness(key: Long) : Long? {
        windowed(size,1,true).forEach { partList ->
            val iterator = partList.iterator()
            var sum = iterator.next()
            val items = mutableListOf(sum)
            while(sum < key && iterator.hasNext()) {
                iterator.next().let {
                    sum += it
                    items += it
                }
            }
            if(sum == key) return items.min()!! + items.max()!!
        }
        return null
    }
    fun Cypher.findFirstInvalid(preambleLength: Int = 25): Long? {
        val validNumbers = take(preambleLength).toMutableList()
        val untested = drop(preambleLength)
        for(item in untested) {
            if(item isSumOfTwoElementsIn validNumbers.takeLast(preambleLength) ) {
                validNumbers += item
            } else {
                return item
            }
        }
        return null
    }
}

private infix fun Long.isSumOfTwoElementsIn(list: List<Long>): Boolean  = list.any { (this - it) in (list - it) }

