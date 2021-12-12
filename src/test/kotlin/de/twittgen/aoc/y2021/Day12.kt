package de.twittgen.aoc.y2021

import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day12 {
    val input by lazy { FileUtil.readInput("2021/day12").parse() }
    val example = """start-A
start-b
A-c
A-b
b-d
A-end
b-end""".parse()

    val example2 = """dc-end
HN-start
start-kj
dc-start
dc-HN
LN-dc
HN-end
kj-sa
kj-HN
kj-dc""".parse()

    val example3 = """fs-end
he-DX
fs-he
start-DX
pj-DX
end-zg
zg-sl
zg-pj
pj-he
RW-he
fs-DX
pj-RW
zg-RW
start-pj
he-WI
zg-he
pj-fs
start-RW""".parse()

    private fun String.parse() = lines().map { line ->
        line.split("-")
    }.map { (first, second) ->
        first.toCave() to second.toCave()
    }

    private fun Cave.getAdjacent(map: List<Pair<Cave,Cave>>) =
        map.mapNotNull { when {
            it.first == this -> it.second
            it.second == this -> it.first
            else -> null
        } }

    private fun List<Pair<Cave,Cave>>.findPaths(
        allowSmallDoubles: Boolean = false,
        previous: List<Cave> = listOf(Start),
    ): List<List<Cave>> {
        if (previous.last() == End) return listOf(previous)
        val adjacent = previous
            .last()
            .getAdjacent(this)
            .filterNot { it in getBlockedCaves(previous, allowSmallDoubles) }
        if (adjacent.isEmpty()) return listOf()
        return adjacent.flatMap { next ->
            findPaths(allowSmallDoubles, previous + next)
        }.filterNot { it.isEmpty() }
    }

    private fun getBlockedCaves(path: List<Cave>, allowSmallDoubles: Boolean): List<Cave> {
        val spentSmall = path.filterIsInstance<Small>()
        return if(!allowSmallDoubles || (spentSmall.hasDouble())) {
            (spentSmall + Start)
        }else {
            listOf(Start)
        }
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





