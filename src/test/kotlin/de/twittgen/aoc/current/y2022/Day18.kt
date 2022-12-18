package de.twittgen.aoc.current.y2022

import de.twittgen.aoc.current.Day
import de.twittgen.aoc.util.Point3D
import de.twittgen.aoc.util.boundaries

class Day18: Day<List<Point3D>>() {
    override fun String.parse() = lines().map { l -> l.split(",").map { it.toInt() }
        .let { (x,y,z) -> Point3D(x,y,z) } }

    init {
        part1(64, 3500) { it.surfaceArea() }
        part2(58, 2048) {cubes -> cubes.surfaceArea() - cubes.getAirPockets().sumOf { it.surfaceArea() }}
    }

    private fun List<Point3D>.surfaceArea() = 6 * size - (sumOf { c -> c.getAdjacent().count { it in this } })

    private fun List<Point3D>.getAirPockets() : List<List<Point3D>> {
        val xR = minOf { it.x }..maxOf { it.x }
        val yR = minOf { it.y }..maxOf { it.y }
        val zR = minOf { it.z }..maxOf { it.z }
        val allAir = xR.flatMap { x -> yR.flatMap { y -> zR.map { z ->
            Point3D(x,y,z)
        } } } - this
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
                current.addAll(new)
                remaining.removeAll(new)
            } else {
                groups.add(current.toList())
                current.clear()
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