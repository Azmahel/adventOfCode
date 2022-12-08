package de.twittgen.aoc.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.second

class Day5 : Day<Int, Int, List<Day5.LineDef>>(){
    override val example = """
        0,9 -> 5,9
        8,0 -> 0,8
        9,4 -> 3,4
        2,2 -> 2,1
        7,0 -> 7,4
        6,4 -> 2,0
        0,9 -> 2,9
        3,4 -> 1,4
        0,0 -> 8,8
        5,5 -> 8,2
    """.trimIndent()

    override fun String.parse(): List<LineDef> {
        return lines().map { toLineDef() }
    }

    private fun String.toLineDef() = split(" -> ")
            .map { it.split(",").map(String::toInt).run { first() to second() } }
            .run { LineDef(first(), second()) }

    init {
        part1(5, 5690) {
            filter { it.isVertical() || it.isHorizontal() }
                .map { it.toLine() }
                .intersections()
                .size
        }
        part2(12, 17741) {
            map { it.toLine() }.intersections().size
        }
    }


    private fun LineDef.isHorizontal() = a.second == b.second
    private fun LineDef.isVertical() = a.first == b.first
    private fun LineDef.toLine(): Line {
       return Line(when {
            isHorizontal() -> range(a.first, b.first).map { it to a.second }
            isVertical() -> range(a.second, b.second).map { a.first to it }
            else -> range(a.first, b.first).zip(range(a.second,b.second))
        })
    }

    private fun range(a:Int, b:Int) = if(a<b) a..b else a downTo b

    data class LineDef(val a: Point, val b: Point)
    data class Line(val points: List<Point>)

    private fun List<Line>.intersections() =
        flatMap { line -> line.points.map { point -> point to line } }
            .groupBy { it.first }
            .entries
            .filter { (_, v) -> v.size > 1 }
            .map { (k, _) -> k }
}

typealias Point = Pair<Int,Int>
