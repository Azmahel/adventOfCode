package de.twittgen.aoc.y2021

import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day12 {
    val input by lazy { FileUtil.readInput("2021/day12").parse() }
    val example = "start-A\nstart-b\nA-c\nA-b\nb-d\nA-end\nb-end".parse()
    val example2 = "dc-end\nHN-start\nstart-kj\ndc-start\ndc-HN\nLN-dc\nHN-end\nkj-sa\nkj-HN\nkj-dc".parse()
    val example3 = "fs-end\nhe-DX\nfs-he\nstart-DX\npj-DX\nend-zg\nzg-sl\nzg-pj\npj-he\nRW-he\nfs-DX\npj-RW\nzg-RW\nstart-pj\nhe-WI\nzg-he\npj-fs\nstart-RW".parse()

    private fun String.parse() = lines().map { line -> line.split("-") }.map { (first, second) ->
        first.toCave() to second.toCave()
    }

    private fun Cave.getAdjacent(map: List<Pair<Cave,Cave>>) =
        map.mapNotNull { when {
            it.first == this -> it.second
            it.second == this -> it.first
            else -> null
        } }

    private  fun List<Pair<Cave,Cave>>.findPaths(
        allowSmallDoubles: Boolean = false,
        previous: List<Cave> = listOf(Start),
    ): List<List<Cave>> {
        if (previous.last() == End) return listOf(previous)
        val adjacent = previous.last().getAdjacent(this).filterNot { it in getBlocked(previous, allowSmallDoubles) }
        return adjacent.flatMap { findPaths(allowSmallDoubles, previous + it) }.filterNot { it.isEmpty() }
    }

    private fun getBlocked(path: List<Cave>, allowSmallDoubles: Boolean): List<Cave> {
        val spentSmall = path.filterIsInstance<Small>()
        return if(!allowSmallDoubles || (spentSmall.hasDouble())) (spentSmall + Start) else listOf(Start)
    }

    private fun  List<Cave>.hasDouble(): Boolean = toSet().size != size

    private fun String.toCave() = when(this) {
        "start" -> Start
        "end" -> End
        else -> if (isLowerCase()) Small(this) else Large(this)
    }

    sealed class Cave
    object Start : Cave()
    object End : Cave()
    data class Small(val name: String) : Cave()
    data class Large(val name: String) : Cave()
    private fun String.isLowerCase() = lowercase() == this

    @Test
    fun example() {
        val result1 = example.findPaths()
        assertEquals(10, result1.size)
        val result2 = example2.findPaths()
        assertEquals(19, result2.size)
        val result3 = example3.findPaths()
        assertEquals(226, result3.size)
    }

    @Test
    fun example2() {
        val result1 = example.findPaths(true)
        assertEquals(36, result1.size)
        val result2 = example2.findPaths(true)
        assertEquals(103, result2.size)
        val result3 = example3.findPaths(true)
        assertEquals(3509, result3.size)
    }

    @Test
    fun part1() {
        val result = input.findPaths()
        println(result.size)
    }

    @Test
    fun part2() {
        val result = input.findPaths(true)
        println(result.size)
    }
}





