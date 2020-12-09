package de.twittgen.aoc.y2020

import org.junit.jupiter.api.Test
import de.twittgen.aoc.y2019.shared.util.FileUtil
class Day1 {
    @Test
    fun basicTest() {
        var x = findSummands(9, emptyList()).first()
        assert(x == null)
        x= findSummands(3, listOf(1,2)).first()
        assert(x == 1)
        x= findSummands(3,listOf(2,2)).first()
        assert(x == null)
    }

    @Test
    fun example() {
        val x = findSummands(2020, listOf(
            1721,
            979,
            366,
            299,
            675,
            1456
        )).first()
        assert((x!!*(2020-x)) == 514579)
    }

    @Test
    fun real() {
        val list = FileUtil.readInput("2020/day1").lines().map { it.toInt(10) }
        val x = findSummands(2020,list).first()
       println(x!!*(2020-x))
    }

    @Test
    fun part2() {
        val list = FileUtil.readInput("2020/day1").lines().map { it.toInt(10) }

        val x = findThreeSummands(2020, list).take(3).fold(1) { x,y -> x*y }
        println(x)
    }

}

fun findThreeSummands(i: Int, list: List<Int>): List<Int> {

    return list.filter {
        findSummands(2020 - it, list).isNotEmpty()
    }
}

fun findSummands(i: Int, list: List<Int>): List<Int> {
    return list.filter {
        i - it in list
    }
}
