package de.twittgen.aoc.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.FileUtil
import de.twittgen.aoc.util.toIntRange
import de.twittgen.aoc.y2021.Day17.TargetArea
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.max

typealias Trace = List<Pair<Int,Int>>

class Day17 : Day<Int, Int, TargetArea>(){
    override val example = """target area: x=20..30, y=-10..-5"""
    override fun String.parse() = split(", ").let { (a, b) ->
        TargetArea(a.takeLastWhile { it != '=' }.toIntRange("..") , b.takeLastWhile { it!= '=' }.toIntRange(".."))
    }

    init {
        part1(45, 12561) { getHittingShots().getHighest() }
        part2(112, 3785) { getHittingShots().size }
    }

    data class TargetArea(val targetX : IntRange, val targetY: IntRange)

    private fun Int.xTrace() = (this downTo 1).runningFold(0) { s, it -> s+it}

    private fun TargetArea.getHittingShots(): List<Trace> {
        val possibleX = (1..targetX.last).filter { it.xTrace().any { it in targetX }  }
        return possibleX.flatMap { x -> (targetY.first..(-targetY.first)).map { y -> x to y } }
            .map { it.shoot(targetY.minOf { it }) }
            .filter { it.any {(x,y) -> x in targetX && y in targetY} }
    }

    private fun List<Trace>.getHighest() =maxOf { it.maxOf { it.second } }

    private fun Pair<Int,Int>.shoot(minY: Int, current: Pair<Int, Int> = 0 to 0) : Trace {
        val next = current.first + first to current.second + second
        return if(next.second < minY) {
            listOf(current)
        } else {
            listOf(current) + (max(first - 1,0) to second - 1).shoot(minY, next)
        }
    }
}

