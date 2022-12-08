package de.twittgen.aoc.y2021

import de.twittgen.aoc.util.FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.max

class Day17 {
    //Assuming shots in positive x direction

    val input by lazy { FileUtil.readInput("2021/day17").parse() }
    val example = """target area: x=20..30, y=-10..-5""".parse()
    val start = 0 to 0
    private fun String.parse(): TargetArea {
        val (a,b) = split(", ")
        return TargetArea(a.takeLastWhile { it != '=' }.toRange() , b.takeLastWhile { it!= '=' }.toRange())
    }

    data class TargetArea(val targetX : IntRange, val targetY: IntRange)
    private fun String.toRange(): IntRange {
        val (a,b) = split("..")
        return (a.toInt()..b.toInt())
    }

    private fun Int.xTrace() = (this downTo 1).runningFold(0) { s, it -> s+it}

    private fun TargetArea.getHittingShots(): List<List<Pair<Int, Int>>> {
        val possibleX = (1..targetX.last).filter { it.xTrace().any { it in targetX }  }
        return possibleX.flatMap { x ->
            //if velY < targety.first it will be lower than area even after first shot.
            // positive velY shots will have -velY velocity when they reach y=0
            (targetY.first..(-targetY.first)).map { y -> x to y }
        }.map {
           it.shoot(targetY.minOf { it })
        }.filter {
            it.any {(x,y) -> x in targetX && y in targetY}
        }
    }

    fun List<List<Pair<Int,Int>>>.getHighest() =maxOf { it.maxOf { it.second } }

    private fun Pair<Int,Int>.shoot(minY: Int, current: Pair<Int, Int> = start) : List<Pair<Int,Int>> {
        val next = current.first + first to current.second + second
        return if(next.second < minY) listOf(current)
        else listOf(current) + (max(first - 1,0) to second - 1).shoot(minY, next)
    }

    @Test
    fun example() {
        val result = example.getHittingShots().getHighest()
        assertEquals(45, result)
    }

    @Test
    fun example2() {
        val result = example.getHittingShots().size
        assertEquals(112, result)
    }

    @Test
    fun part1() {
        val result = input.getHittingShots().getHighest()
        println(result)
    }

    @Test
    fun part2() {
        val result = input.getHittingShots().size
        println(result)
    }
}

