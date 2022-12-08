package de.twittgen.aoc.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.filterIn

typealias EnergyLevels = Map<Pair<Int, Int>, Int>

class Day11 : Day<Int, Int, EnergyLevels>() {
    override fun String.parse() = lines().map { line-> line.map { it.digitToInt() } }.toMap()
    private fun List<List<Int>>.toMap() = flatMapIndexed { x, it -> it.mapIndexed { y, v -> (x to y) to v } }.toMap()

    init {
        part1(1656, ){ runSteps(100) }
        part2(195, ) { findSynchronisation() }
    }

    private fun Pair<Int,Int>.getAdjacency() =
        (-1..1).flatMap { a -> (-1..1).map { b -> (first + a) to (second + b) } }.minus(this)

    private fun EnergyLevels.singleStep(): EnergyLevels = mapValues { (_, v) -> v+1 }.subStep()

    private tailrec fun EnergyLevels.subStep(flashed: Set<Pair<Int, Int>> = emptySet()) : EnergyLevels {
        return if (none { it.readyToFlash(flashed) }) {
            mapValues { (k,v) -> if (k in flashed) 0 else v }
        } else {
            val flashing = filter { it.readyToFlash(flashed) }.keys
            val adjacency = flashing.flatMap {
                it.getAdjacency() }.groupBy { it }.mapValues { (_,v) -> v.size  }
            mapValues { (k,v) ->  v + adjacency.getOrDefault(k, 0) }.subStep(flashed + flashing)
        }
    }

    private fun Map.Entry<Pair<Int, Int>, Int>.readyToFlash(flashed: Set<Pair<Int, Int>>) = value > 9 && key !in flashed

    private tailrec fun EnergyLevels.runSteps(i: Int, flashes: Int = 0): Int {
         if (i==0) {
             return flashes
        } else {
            with(singleStep()) { return runSteps(i-1, values.count { it == 0 } + flashes) }
        }
    }

    private tailrec fun EnergyLevels.findSynchronisation(steps: Int = 0): Int {
        return if (values.all { it == 0 }) steps else singleStep().findSynchronisation(steps+1)
    }

    override val example = """
        5483143223
        2745854711
        5264556173
        6141336146
        6357385478
        4167524645
        2176841721
        6882881134
        4846848554
        5283751526
    """.trimIndent()
}

