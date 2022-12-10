package de.twittgen.aoc.current.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D


class Day11 : Day<EnergyLevels>() {
    override fun String.parse() = lines().map { line-> line.map { it.digitToInt() } }.toMap()
    private fun List<List<Int>>.toMap() = flatMapIndexed { x, it -> it.mapIndexed { y, v -> Point2D(x, y) to v } }.toMap()

    init {
        part1(1656, 1667){ it.runSteps(100) }
        part2(195, 488) { it.findSynchronisation() }
    }

    private fun Pair<Int,Int>.getAdjacency() =
        (-1..1).flatMap { a -> (-1..1).map { b -> (first + a) to (second + b) } }.minus(this)

    private fun EnergyLevels.doStep(): EnergyLevels = mapValues { (_, v) -> v+1 }.subStep()

    private tailrec fun EnergyLevels.subStep(flashed: Set<Point2D> = emptySet()) : EnergyLevels {
        return if (none { it.readyToFlash(flashed) }) {
            mapValues { (k,v) -> if (k in flashed) 0 else v }
        } else {
            val flashing = filter { it.readyToFlash(flashed) }.keys
            val adjacency = flashing.flatMap { it.adjacent() }.groupBy { it }.mapValues { (_,v) -> v.size  }
            mapValues { (k,v) ->  v + adjacency.getOrDefault(k, 0) }.subStep(flashed + flashing)
        }
    }

    private fun Map.Entry<Point2D, Int>.readyToFlash(flashed: Set<Point2D>) = value > 9 && key !in flashed

    private tailrec fun EnergyLevels.runSteps(i: Int, flashes: Int = 0): Int {
         return if (i==0) {
             flashes
         } else {
             val next = doStep()
             next.runSteps(i-1, flashes + next.values.count { it == 0 } )
         }
    }

    private tailrec fun EnergyLevels.findSynchronisation(steps: Int = 0): Int {
        return if (values.all { it == 0 }) steps else doStep().findSynchronisation(steps+1)
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
private typealias EnergyLevels = Map<Point2D, Int>

