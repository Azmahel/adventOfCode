package de.twittgen.aoc.current.y2021

import de.twittgen.aoc.current.Day

class Day09 : Day<Heightmap>() {
    override fun String.parse() = lines().map {line -> line.map { it.digitToInt() } }.toMap()
    private fun List<List<Int>>.toMap() = flatMapIndexed { x, it -> it.mapIndexed { y, v -> (x to y) to v } }.toMap()

    init {
        part1(15, 535) { it.findLowPoints().values.sumOf { it+1 } }
        part2(1134, 1122700) { it.getBasins(it.findLowPoints()).getScore() }
    }

    private fun Heightmap.getBasins(lowPoints: Heightmap): List<Basin> = growBasins(lowPoints.keys.map { mutableSetOf(it) })

    private tailrec fun Heightmap.growBasins(basins :List<Basin>) : List<Basin> {
        return if (this.isComplete(basins)) {
            basins
        } else {
            this.growBasins(
                basins.map { basin ->
                    basin + basin.flatMap { point -> point.getAdjacency().filter { getOrDefault(it, 9) != 9 } }
                }.distinct()
            )
        }
    }

    private fun Pair<Int,Int>.getAdjacency() = listOf((first-1) to second, (first+1) to second, first to (second-1), first to (second+1))
    private fun Heightmap.findLowPoints() = filter { (c , i) -> c.getAdjacency().mapNotNull { this[it] }.all { it > i } }
    private fun List<Basin>.getScore() = sortedByDescending { it.size }.take(3).fold(1) { i, it -> i * it.size }
    private fun Heightmap.isComplete(basins: List<Basin>) = minus(basins.flatten().toSet()).all { (_, v) -> v == 9 }

    override val example = """
        2199943210
        3987894921
        9856789892
        8767896789
        9899965678
        """.trimIndent()
}
private typealias Heightmap = Map<Pair<Int,Int>, Int>
private typealias Basin = Set<Pair<Int, Int>>

