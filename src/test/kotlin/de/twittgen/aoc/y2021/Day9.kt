package de.twittgen.aoc.y2021

import de.twittgen.aoc.Day

typealias Heightmap = Map<Pair<Int,Int>, Int>
typealias Basin = Set<Pair<Int, Int>>

class Day9 : Day<Int, Int, Heightmap>() {

    override val example = """
        2199943210
        3987894921
        9856789892
        8767896789
        9899965678
        """.trimIndent()

    override fun String.parse() = lines().map {line -> line.map { it.digitToInt() } }.toMap()
    private fun List<List<Int>>.toMap() = flatMapIndexed { x, it -> it.mapIndexed { y, v -> (x to y) to v } }.toMap()

    init {
        part1(15, 535) {
            findLowPoints().values.sumOf { it+1 }
        }

        part2(1134, 1122700) {
            getBasins(findLowPoints()).getScore()
        }
    }

    private fun Pair<Int,Int>.getAdjacency() =
        listOf((first-1) to second, (first+1) to second, first to (second-1), first to (second+1))

    private fun Heightmap.findLowPoints() =
        filter { (coord , i) -> coord.getAdjacency().mapNotNull { this[it] }.all { it > i } }


    private fun Heightmap.getBasins(lowPoints: Heightmap): List<Basin> {
        val basins = lowPoints.keys.map { mutableSetOf(it) }
        while(!this.isComplete(basins)) {
            basins.map { basin ->
                basin.addAll(
                    basin
                        .flatMap { coord -> coord.getAdjacency().filter { (this[it] ?: 9) !=9 } }
                        .distinct()
                )
            }
        }
        return basins
    }

    private fun List<Basin>.getScore() =
        sortedByDescending { it.size }.take(3).fold(1) { i, it -> i * it.size }

    private fun Heightmap.isComplete(basins: List<Basin>) =
        minus(basins.flatten().toSet()).all { (_, v) -> v == 9 }
}

