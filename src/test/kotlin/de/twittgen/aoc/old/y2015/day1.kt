package de.twittgen.aoc.old.y2015

import de.twittgen.aoc.util.FileUtil
import org.junit.jupiter.api.Test

class day1 {
    val input = FileUtil.readInput("2015/day1")
    val examples = listOf(
        "(())" to 0,
        "()()" to 0,
        "(((" to 3,
        "(()(()(" to 3,
        "))(((((" to 3,
        "())" to -1,
        "))(" to -1,
        ")))" to -3,
        ")())())" to -3
    )

    @Test
    fun examples(){
        val results = examples.map { floorNumber(it.first) - it.second }
        assert(
            results.all { it ==0 }
        )
    }
    @Test
    fun part1() {
        val result = floorNumber(input)
        println(result)
    }

    @Test
    fun part2() {
        val result = findBasement(input)
        println(result)
    }

    private fun floorNumber(s: String): Int {
        return s.count { it == '(' } - s.count { it == ')'}
    }

    private fun findBasement(s: String): Int {
        return s.asSequence()
            .mapIndexed { i, _ -> floorNumber(s.take(i+1)) }
            .indexOfFirst { it<0 } +1
    }
}