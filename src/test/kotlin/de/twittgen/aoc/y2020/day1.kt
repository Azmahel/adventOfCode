package de.twittgen.aoc.y2020

import org.junit.jupiter.api.Test
import de.twittgen.aoc.util.FileUtil
class Day1 {
    val input = FileUtil.readInput("2020/day1")
    val example = listOf(1721, 979, 366, 299, 675, 1456)

    @Test
    fun example() {
        val x = findSummands(2020, example).first()
        assert((x!!*(2020-x)) == 514579)
    }

    @Test
    fun part1() {
        val list = input.lines().map { it.toInt(10) }
        val x = findSummands(2020,list).first()
       println(x!!*(2020-x))
    }

    @Test
    fun part2() {
        val list = input.lines().map { it.toInt(10) }

        val x = findThreeSummands(2020, list)
            .take(3)
            .fold(1) { x,y -> x*y }
        println(x)
    }

}

fun findThreeSummands(i: Int, list: List<Int>): List<Int> {
    return list.filter {
        findSummands(i - it, list).isNotEmpty()
    }
}

fun findSummands(i: Int, list: List<Int>): List<Int> {
    return list.filter {
        i - it in list
    }
}
