package de.twittgen.aoc.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.hasDuplicate
import de.twittgen.aoc.util.isLowerCase
import de.twittgen.aoc.util.mapLines
import de.twittgen.aoc.y2021.Day12.Cave

class Day12: Day<CaveMap>() {
    override fun String.parse() = mapLines { it.split("-").let { (a, b) -> a.toCave() to b.toCave() } }.toSet()

    init {
        part1(226, 4167) { it.findPaths { singleUseSmallCaves() }.size }
        part2(3509, 98441) { it.findPaths { oneSmallCanBeRevisited() }.size }
    }

    private fun Cave.getAdjacent(map: CaveMap) =
        map.mapNotNull { when {
            it.first == this -> it.second
            it.second == this -> it.first
            else -> null
        } }

    private fun CaveMap.findPaths(current: Path = listOf(Start), getBlocked : Path.() -> Set<Cave>): List<Path> {
        if (current.last() == End) return listOf(current)
        return current
            .last()
            .getAdjacent(this)
            .filterNot { it in current.getBlocked()}
            .flatMap { findPaths(current + it, getBlocked) }.filterNot { it.isEmpty() }
    }

    private val singleUseSmallCaves : Path.() -> Set<Cave> = { (filterIsInstance<Small>() + Start).toSet() }
    private val oneSmallCanBeRevisited : Path.() -> Set<Cave> = {
        filterIsInstance<Small>().let { if((it.hasDuplicate())) (it + Start) else listOf(Start) }.toSet()
    }

    private fun String.toCave() = when(this) {
        "start" -> Start
        "end" -> End
        else -> if (isLowerCase()) Small(this) else Large(this)
    }

    sealed class Cave
    data object Start : Cave()
    data object End : Cave()
    data class Small(val name: String) : Cave()
    data class Large(val name: String) : Cave()

    override val example = """
        fs-end
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
        start-RW
    """.trimIndent()
}
private typealias CaveMap = Set<Pair<Cave, Cave>>
private typealias Path = List<Cave>





