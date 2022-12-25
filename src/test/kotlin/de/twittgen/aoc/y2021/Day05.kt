package de.twittgen.aoc.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.y2021.Day05.Line

class Day05 : Day<List<Line>>(){
    override fun String.parse() = lines().map { it.toLine() }

    private fun String.toLine() = split(" -> ")
            .map { it.split(",").map(String::toInt).let {(x,y) -> Point2D(x, y) } }
            .let { (x,y) ->  LineDef(x, y) }.toLine()

    init {
        part1(5, 5690) {
            it.filter {line ->  line.isVertical() || line.isHorizontal() }.intersections().size
        }
        part2(12, 17741) { it.intersections().size }
    }

    data class LineDef(val a: Point2D, val b: Point2D) {
        fun toLine()= Line(when {
            a.y == b.y -> range(a.x, b.x).map { Point2D(it, a.y) }
            a.x == b.x -> range(a.y, b.y).map { Point2D(a.x, it) }
            else -> range(a.x, b.x).zip(range(a.y, b.y)).map(Point2D::of)
        })
        private fun range(a:Int, b:Int) = if(a<b) a..b else a downTo b
    }
    data class Line(val points: List<Point2D>) {
        fun isHorizontal() = points.first().y == points.last().y
        fun isVertical() = points.first().x == points.last().x
    }

    private fun List<Line>.intersections() = flatMap { line -> line.points.map { point -> point to line } }
        .groupBy { it.first }
        .filterValues { it.size > 1 }
        .keys

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
}

