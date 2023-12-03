package de.twittgen.aoc.y2023

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.isADigit
import de.twittgen.aoc.util.mapCoordinates
import de.twittgen.aoc.util.second

class Day03 : Day<Schematic>() {
    override fun String.parse(): Map<Point2D, Char> = mapCoordinates { y, x, c ->  when(c) {
        '.' -> null
        else -> Point2D(x, y) to c
    } }.toMap()

    init {
        part1(4361, 556367) { s -> s.getPartLocations().getSerialNumbers(s).sum() }
        part2(467835L, 89471771) { s -> s.getGearRatios().sum() }
    }

    private fun Schematic.getGearRatios() = entries
    .filter { it.value == '*' }
        .map { listOf(it.key).getSerialNumbers(this) }
        .filter { it.size == 2 }
        .map { it.first().toLong() * it.second().toLong() }

    private fun List<Point2D>.getSerialNumbers(s: Schematic) =
        getAdjacentNumbers(s).map { s.findNumberAt(it) }.distinct().map { it.toSerialNumber(s) }

    private fun Schematic.getPartLocations() = filter { !it.value.isDigit() }.map { it.key }

    private fun List<Point2D>.getAdjacentNumbers(s: Schematic) =
        flatMap { it.adjacent() }.filter { s[it].isADigit()  }.toSet()

    private fun List<Point2D>.toSerialNumber(s: Schematic) = mapNotNull { s[it] }.joinToString("").toInt()

    private fun Schematic.findNumberAt(x: Point2D): List<Point2D> {
        if (!(get(x).isADigit())) return emptyList()
        return discoverNumber(x, Point2D.Direction.LEFT).reversed() + x + discoverNumber(x, Point2D.Direction.RIGHT)
    }

    private fun Schematic.discoverNumber(x: Point2D, direction: Point2D.Direction): List<Point2D> {
        var pivot = direction.next(x)
        val leftSide = mutableListOf<Point2D>()
        while (get(pivot).isADigit()) {
            leftSide.add(pivot).also { pivot = direction.next(pivot) }
        }
        return leftSide
    }

    override val example = """
        467..114..
        ...*......
        ..35..633.
        ......#...
        617*......
        .....+.58.
        ..592.....
        ......755.
        ...${'$'}.*....
        .664.598..
    """.trimIndent()
}
typealias Schematic = Map<Point2D, Char>