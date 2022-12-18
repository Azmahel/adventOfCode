package de.twittgen.aoc.current.y2020

import de.twittgen.aoc.current.Day

class Day17 : Day<Set<SpaceTimeCoordinate>>() {
    override fun String.parse() = lines().mapIndexed { x, line -> line.mapIndexedNotNull { y, c ->
        if (c=='#') listOf(x,y) else null
    } }.flatten().toSet()

    init {
        part1(112, 286) { it.run().size }
        part2(848, 960) { it.run(4).size}
    }

    private fun Set<SpaceTimeCoordinate>.getNeighbours(dimensions: Int) = flatMap { it.getNeighbours(dimensions) }.toSet()

    private fun SpaceTimeCoordinate.getNeighbours(dimensions: Int): Set<SpaceTimeCoordinate> {
       val deltas = getDeltas(dimensions)
       val neighbours= deltas
           .map { d -> d.mapIndexed { i , di -> di + (getOrNull(i) ?: 0)  }
           .dropLastWhile { it ==0 } }.toSet()
        return neighbours
    }

    private val deltas: MutableMap<Int,List<SpaceTimeCoordinate>> = mutableMapOf()
    private val base = -1..1
    private fun getDeltas(dimensions: Int) = deltas.getOrPut(dimensions) {
        var current = base.map { listOf(it) }
        repeat(dimensions-1) { current = current.flatMap { base.map { next -> it +next } } }
        current
    }

    fun Set<SpaceTimeCoordinate>.run(dimensions: Int = 3): Set<SpaceTimeCoordinate> {
        var map  = this.map { c -> c.dropLastWhile { it == 0 } }.toSet()
        repeat(6) {
            val relevantPoints = map.getNeighbours(dimensions)
            map = relevantPoints.mapNotNull { point ->
                val activeNeighbours = point.getNeighbours(dimensions).filterNot{ it == point }.filter { it in map }
                val range = if (point in map) 2..3 else 3..3
                if (activeNeighbours.size in range) point else null
            }.toSet()
        }
        return map
    }

    override val example = """
        .#.
        ..#
        ###
    """.trimIndent()
}
private typealias  SpaceTimeCoordinate = List<Int>
