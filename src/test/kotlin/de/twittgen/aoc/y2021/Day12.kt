package de.twittgen.aoc.y2021

import de.twittgen.aoc.util.FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day12 {
    val input by lazy { FileUtil.readInput("2021/day12").parse() }
    val example = "start-A\nstart-b\nA-c\nA-b\nb-d\nA-end\nb-end".parse()
    val example2 = "dc-end\nHN-start\nstart-kj\ndc-start\ndc-HN\nLN-dc\nHN-end\nkj-sa\nkj-HN\nkj-dc".parse()
    val example3 = "fs-end\nhe-DX\nfs-he\nstart-DX\npj-DX\nend-zg\nzg-sl\nzg-pj\npj-he\nRW-he\nfs-DX\npj-RW\nzg-RW\nstart-pj\nhe-WI\nzg-he\npj-fs\nstart-RW".parse()

    private fun String.parse() = lines().map { it.split("-") }.map { (a, b) -> a.toCave() to b.toCave() }

    private fun Cave.getAdjacent(map: List<Pair<Cave,Cave>>) =
        map.mapNotNull { when {
            it.first == this -> it.second
            it.second == this -> it.first
            else -> null
        } }

    private  fun List<Pair<Cave,Cave>>.findPaths(allowSmallDouble: Boolean = false, previous: List<Cave> = listOf(Start),
    ): List<List<Cave>> {
        if (previous.last() == End) return listOf(previous)
        val adjacent = previous.last().getAdjacent(this).filterNot { it in previous.blocked(allowSmallDouble) }
        return adjacent.flatMap { findPaths(allowSmallDouble, previous + it) }.filterNot { it.isEmpty() }
    }

    private fun List<Cave>.blocked(allowSmallDouble: Boolean): List<Cave> {
        val spentSmall = filterIsInstance<Small>()
        return if(!allowSmallDouble || (spentSmall.hasDouble())) (spentSmall + Start) else listOf(Start)
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
        val result = listOf(example.findPaths(), example2.findPaths(), example3.findPaths())
        assertEquals(listOf(10, 19, 226), result.map{ it.size })
    }

    @Test
    fun example2() {
        val result = listOf(example.findPaths(true), example2.findPaths(true), example3.findPaths(true))
        assertEquals(listOf(36, 103, 3509), result.map { it.size })
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





