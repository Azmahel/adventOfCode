package de.twittgen.aoc.y2023

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.Point2D.Companion.ORIGIN
import de.twittgen.aoc.util.Point2D.Direction.entries
import de.twittgen.aoc.util.mapLines
import de.twittgen.aoc.y2023.Day18.Instruction

class Day18: Day<Polygon>() {
    override fun String.parse() = mapLines {
        it.split(" ").let { (a,b,c) -> Instruction(a.toDirection(), b.toLong(), c) }
    }
    private fun String.toDirection() = entries.first { it.s == this }

    init {
        part1(62, 42317) { picksTheorem(it) }
        part2(952408144115, 83605563360288) { picksTheorem(it.map { i -> i.fix() }) }
    }

    private fun picksTheorem(it: Polygon) = it.area() + (it.perimeter() / 2) + 1L
    private fun Polygon.area() = toVertices().shoelace()

    private val colorRegex = Regex("\\(#(.{5})(.)\\)")
    private fun Instruction.fix() = colorRegex.matchEntire(color)!!.groupValues.let { (_,a,b) ->
        Instruction(when (b) {"0" -> "R" "1" -> "D" "2" -> "L" else -> "U" }.toDirection(), a.toLong(16), "")
    }

    private fun Polygon.toVertices() = runningFold(0L to 0L) { acc, (dir, dist) ->
        (dir.next(ORIGIN) * dist.toInt()).let { (x, y) -> acc.first + x.toLong() to acc.second - y.toLong() }
    }

    private fun Vertices.shoelace() = zipWithNext().sumOf { (a, b) -> (a.first * b.second) - (a.second * b.first) } / 2
    private fun Polygon.perimeter() = sumOf { it.dist }

    override val example = """
        R 6 (#70c710)
        D 5 (#0dc571)
        L 2 (#5713f0)
        D 2 (#d2c081)
        R 2 (#59c680)
        D 2 (#411b91)
        L 5 (#8ceee2)
        U 2 (#caa173)
        L 1 (#1b58a2)
        U 2 (#caa171)
        R 2 (#7807d2)
        U 3 (#a77fa3)
        L 2 (#015232)
        U 2 (#7a21e3)
    """.trimIndent()
    data class Instruction(val dir: Point2D.Direction, val dist: Long, val color: String)

}
private typealias Polygon = List<Instruction>
private typealias Vertices = List<Pair<Long,Long>>