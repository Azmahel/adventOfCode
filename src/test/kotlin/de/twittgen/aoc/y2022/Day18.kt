package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point3D
import de.twittgen.aoc.util.boundaries
import de.twittgen.aoc.util.toPoint3d

class Day18: Day<List<Point3D>>() {
    override fun String.parse() = lines().map { l -> l.toPoint3d() }

    init {
        part1(64, 3500) { it.surface() }
        part2(58, 2048) {cubes -> cubes.surface() - cubes.getAirPockets().sumOf { it.surface() } }
    }

    private fun List<Point3D>.surface() = 6 * size - (sumOf { c -> c.getAdjacent().count { it in this } })

    private fun List<Point3D>.getAirPockets() : List<List<Point3D>> {
        val (xR, yR, zR) = listOf(minOf{it.x}..maxOf{it.x}, minOf{it.y}..maxOf{it.y}, minOf {it.z}..maxOf {it.z})
        val allAir = xR.flatMap { x -> yR.flatMap { y -> zR.map { z -> Point3D(x,y,z) } } } - this.toSet()
        return allAir.findGroups().filterNot {p ->
            p.any { (x,y,z) -> x in xR.boundaries() || y in yR.boundaries() ||z in zR.boundaries()}
        }
    }

    private fun List<Point3D>.findGroups() : List<List<Point3D>> {
        val groups = mutableListOf<List<Point3D>>()
        val remaining = this.toMutableList()
        val current = mutableListOf<Point3D>()
        while(remaining.isNotEmpty()) {
            if (current.isEmpty()) current += remaining.first().also { remaining.removeAt(0) }
            val new = current.flatMap { it.getAdjacent() }.distinct().filter { it in remaining }
            if (new.isNotEmpty()) {
                current.addAll(new).also { remaining.removeAll(new) }
            } else {
                groups.add(current.toList()).also { current.clear() }
            }
        }
        return groups
    }

    override val example = """
        2,2,2
        1,2,2
        3,2,2
        2,1,2
        2,3,2
        2,2,1
        2,2,3
        2,2,4
        2,2,6
        1,2,5
        3,2,5
        2,1,5
        2,3,5
    """.trimIndent()
}