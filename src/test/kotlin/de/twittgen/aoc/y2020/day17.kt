package de.twittgen.aoc.y2020

import de.twittgen.aoc.util.FileUtil
import org.junit.jupiter.api.Test

typealias  SpaceTimeCoordinate = List<Int>
class day17 {
    val input = FileUtil.readInput("2020/day17")
    val example = """
        .#.
        ..#
        ###
    """.trimIndent()

    fun parseInput(s: String): Set<SpaceTimeCoordinate> {
       return s.lines().mapIndexed { x, line ->
            line.mapIndexedNotNull { y, c ->
                if (c=='#') listOf(x,y) else null
            }
        }.flatten().toSet()
    }

    @Test
    fun example() {
        var map = parseInput(example)
        map = map.run()
        println(map.size)
    }

    @Test
    fun part1() {
        var map = parseInput(input)
        map = map.run()
        println(map.size)
    }

    @Test
    fun part2() {
        var map = parseInput(input)
        map = map.run(4)
        println(map.size)
    }

    fun Set<SpaceTimeCoordinate>.getNeighbours(dimensions: Int): Set<SpaceTimeCoordinate> = flatMap { it.getNeighbours(dimensions) }.toSet()
    fun SpaceTimeCoordinate.getNeighbours(dimensions: Int): Set<SpaceTimeCoordinate> {
       val deltas = getDeltas(dimensions)
       val neighbours= deltas.map {
               d -> d.mapIndexed { i , di -> di + (getOrNull(i) ?: 0)  }.dropLastWhile { it ==0 }
       }.toSet()
        return neighbours
    }

    val deltamap: MutableMap<Int,List<SpaceTimeCoordinate>> = mutableMapOf()
    val base = -1..1
    fun getDeltas(dimensions: Int): List<SpaceTimeCoordinate> {
        return deltamap.getOrPut(dimensions) {
            var current = base.map { listOf(it) }
            repeat(dimensions-1) {
                current = current.flatMap { base.map { next -> it +next } }
            }
            current
        }

    }
    fun Set<SpaceTimeCoordinate>.run(dimensions: Int = 3): Set<SpaceTimeCoordinate> {
        var map  = this.map { it.dropLastWhile { it == 0 } }.toSet()
        repeat(6) {
            val relevantPoints = map.getNeighbours(dimensions)
            map = relevantPoints.mapNotNull { point ->
                val activeNeighbours = point
                    .getNeighbours(dimensions)
                    .filterNot{
                            it == point
                    }
                    .filter {
                        it in map
                    }
                val range = if (point in map) 2..3 else 3..3
                if (activeNeighbours.size in range) point else null
            }.toSet()
        }
        return map
    }
}