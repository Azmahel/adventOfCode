package de.twittgen.aoc.y2020

import de.twittgen.aoc.util.FileUtil
import org.junit.jupiter.api.Test

class day15 {
    val input= FileUtil.readInput("2020/day15")
    val examples = listOf(
        "0,3,6",
        "1,3,2",
        "2,1,3",
        "1,2,3",
        "2,3,1",
        "3,2,1",
        "3,1,2"
    )
    val expected = listOf(436,1,10,27,78,438,1836)

    fun parseInput(s:String): List<Int> {
        return s.split(',').map { it.toInt() }
    }

    @Test
    fun examples() {
        val results = examples.map(::parseInput).map { playGame(it) }
        results.forEachIndexed { i, it ->
            assert(expected[i] == it)
        }
    }

    @Test
    fun part1() {
        val start = parseInput(input)
        val result = playGame(start)
        println(result)
    }

    @Test
    fun example2() {
        val results = examples.map(::parseInput).map { playGameV2(it, 2020L) }
        results.forEachIndexed { i, it ->
            assert(expected[i] == it.toInt())
        }
    }

    @Test
    fun part2() {
        val start = parseInput(input)
        val result = playGameV2(start)
        println(result)
    }

    fun playGameV2(start:List<Int>, until : Long = 30000000L): Long {
        val occurences = start.mapIndexed { i, it -> it.toLong() to listOf(i.toLong()) }.toMap().toMutableMap()
        var last = start.last().toLong()
        for (i in (start.lastIndex.toLong()) until until-1) {
            last = occurences[last]?.let {
                it.dropLast(1).lastOrNull()?.let{ i- it } ?:  0
            } ?: 0
            occurences[last] = occurences[last]?.lastOrNull()?.let { listOf(it,i+1) } ?: listOf(i+1)
            if( i % 10_000 == 0L ) println(i)
        }
        return last
    }

    fun playGame(start: List<Int> ,until: Int = 2020): Int {
        val current = start.toMutableList()
        for (i in (start.lastIndex) until until-1) {
            val lastSaid = current[i]
            val previousIndex = current.dropLast(1).lastIndexOf(lastSaid)
            current += if(previousIndex == -1) 0 else i-previousIndex
        }
        return current.last()
    }
}