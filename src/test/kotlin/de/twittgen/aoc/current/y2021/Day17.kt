package de.twittgen.aoc.current.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.Point2D.Companion.ORIGIN
import de.twittgen.aoc.util.toIntRange
import de.twittgen.aoc.current.y2021.Day17.TargetArea
import kotlin.math.max

 private typealias Trace = List<Point2D>

class Day17 : Day<Int, Int, TargetArea>(){
    override val example = """target area: x=20..30, y=-10..-5"""
    override fun String.parse() = split(", ").let { (a, b) ->
        TargetArea(a.takeLastWhile { it != '=' }.toIntRange("..") , b.takeLastWhile { it!= '=' }.toIntRange(".."))
    }

    init {
        part1(45, 12561) { getHittingShots().getHighest() }
        part2(112, 3785) { getHittingShots().size }
    }

    data class TargetArea(val targetX : IntRange, val targetY: IntRange) {
        val maxX = targetX.last
        val minY = targetY.first
    }

    private fun Int.traceX() = (this downTo 1).runningFold(0) { s, it -> s + it }

    private fun TargetArea.getHittingShots(): List<Trace> {
        val possibleX = (1..maxX).filter { dX ->  dX.traceX().any { it in targetX }  }
        return possibleX.flatMap { dX -> (minY..(-minY)).map { dY -> Vector(dX, dY) } }
            .map { it.shoot( Point2D(maxX, targetY.minOrNull()!!)) }
            .filter { it.reversed().any { (x,y) -> x in targetX && y in targetY} }
    }

    private fun List<Trace>.getHighest() = maxOf { it.maxOf(Point2D::y) }

    private tailrec fun Vector.shoot(limit: Point2D, from: Point2D = ORIGIN, trace: Trace = listOf(from)) : Trace {
        val next = Point2D(from.x + x , from.y + y)
        return if(next.y < limit.y || next.x > limit.x)  trace else  decrease().shoot(limit, next, trace + next)
    }
}
 private typealias Vector = Point2D
private fun Vector.decrease() = Vector(max(x - 1,0), y - 1)

