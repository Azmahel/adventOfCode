package de.twittgen.aoc.y2023

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.Point2D.Companion.ORIGIN
import de.twittgen.aoc.util.Point2D.Direction.entries
import de.twittgen.aoc.util.mapLines

class Day18: Day<List<Instruction>>() {
    override fun String.parse() =
        mapLines { it.split(" ").let { (a,b,c) -> Instruction(mapDirection(a), b.toLong(), c) } }
    private fun mapDirection(a: String) = entries.first { it.s == a }

    init {
        part1(62, 42317) { picksTheorem(it) }
        part2(952408144115, 83605563360288) { it.map { it.fix() }.let { picksTheorem(it) } }
    }

    private fun picksTheorem(it: List<Instruction>) = it.area() + (it.perimeter() / 2) + 1L
    private fun List<Instruction>.area() = toVertices().shoelace()

    private val colorRegex = Regex("\\(#(.{5})(.)\\)")
    private fun Instruction.fix() = colorRegex.matchEntire(color)!!.groupValues.let { (_,a,b) ->
        Instruction(mapDirection(when(b) { "0" -> "R" "1" -> "D" "2" ->"L" else -> "U" }), a.toLong(16), "")
    }

    private fun List<Instruction>.toVertices() = runningFold(0L to 0L) { acc, (dir, dist) ->
        (dir.next(ORIGIN) * dist.toInt()).let { (x, y) -> acc.first + x.toLong() to acc.second - y.toLong() }
    }

    private fun List<Pair<Long, Long>>.shoelace() = zipWithNext().sumOf { (a, b) -> (a.first * b.second) - (a.second * b.first) } / 2
    private fun List<Instruction>.perimeter() = sumOf { it.dist }

    private fun List<Instruction>.dig(): Set<Point2D> {
        var current = ORIGIN
        val map = mutableSetOf<Point2D>()
        forEach { instruction ->
            (1..instruction.dist).forEach { _ ->
                map.add(current)
                current = instruction.dir.next(current)
            }
        }
        return map
    }

    private fun Set<Point2D>.fill(): Set<Point2D> {
        val xR = minOf { it.x } .. maxOf { it.x }
        val yR = minOf { it.y } .. maxOf { it.y }
        fun Point2D.isInPit() =
            (xR.first..x).count { Point2D(it, y) in this@fill } % 2 == 1 && (yR.first..y).count { Point2D(x, it) in this@fill } % 2 == 1
        fun Point2D.fillMore(): Set<Point2D> {
            val current = mutableSetOf(this)
            var next: List<Point2D>
            do {
                next = current.flatMap { it.adjacent() }.filter { it !in current && it !in this@fill }
                current.addAll(next)
            } while (next.isNotEmpty())
            return current
        }
        return xR.flatMap { x -> yR.mapNotNull { y -> Point2D(x, y) } }
            .first { it !in this && it.isInPit()  }
            .fillMore() + this
    }

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

}

data class Instruction(val dir: Point2D.Direction, val dist: Long, val color: String)