package de.twittgen.aoc.y2021

import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day5 {
    val input by lazy { FileUtil.readInput("2021/day5").parse() }
    val example = """0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2""".parse()

    private fun String.parse(): List<LineDef> {
        return lines().map {
           val (a,b) = it
                .split(" -> ")
                .map {
                    val (a,b) = it.split(",").map { it.toInt() }
                    a to b
                }
            LineDef(a , b)
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

    private fun List<Line>.intersections(): List<Point> {
       val map =  flatMap {line ->
           line.points.map {point ->
               point to line
           }
       }.groupBy { it.first }
        return map.entries.filter { (_,v) -> v.size > 1 }.map { (k,_) -> k }
    }

    @Test
    fun example() {
        val lines = example.filter { it.isVertical() || it.isHorizontal() }.map { it.toLine() }
        val intersections = lines.intersections()
        assertEquals(5, intersections.size)
    }

    @Test
    fun example2() {
        val lines = example.map { it.toLine() }
        val intersections = lines.intersections()
        assertEquals(12, intersections.size)
    }

    @Test
    fun part1() {
        val lines = input.filter { it.isVertical() || it.isHorizontal() }.map { it.toLine() }
        val intersections = lines.intersections()
        println(intersections.size)
    }

    @Test
    fun part2() {
        val lines = input.map { it.toLine() }
        val intersections = lines.intersections()
       println(intersections.size)
    }

}

typealias Point = Pair<Int,Int>
