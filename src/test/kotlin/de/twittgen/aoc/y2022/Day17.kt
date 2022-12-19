package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.Point2D.Companion.ORIGIN
import de.twittgen.aoc.util.cycle
import java.lang.IllegalArgumentException

class Day17: Day<List<Char>>() {
    override fun String.parse() = toList()

    init {
        part1(3068, 3209 ) {
            it.findHeight(2022)
        }
        part2(1514285714288, 1580758017509) {
            it.findHeight(1000000000000)
        }
    }

    class Rock(val positions: Set<Point2D>) {
        fun push(c: Char, blocked: Set<Point2D>) = when(c) {
            '<' -> positions.map { it.left() }.toSet()
                .let { r -> if( r.none { it.x < 0  || it in blocked}) Rock(r) else this }
            '>' ->positions.map { it.right() }.toSet()
                .let { r -> if( r.none { it.x > 6 || it in blocked}) Rock(r) else this }
            else -> throw IllegalArgumentException()
        }
        fun drop(blocked: Set<Point2D>) = positions.map { it.down() }.toSet()
            .let { r -> if ( r.any { it in blocked }) null else Rock(r)
        }
    }

    private fun List<Char>.findHeight(n: Long) : Long {
        val state = State(this)
        val reports = mutableListOf<Triple<Long,Long, Report>>()
        while(state.blockCount < n) {
            state.dropNext()
            val end = Report(state.cShift, state.blockCount % shapes.size, state.heightProfile())
            reports.find { it.third == end }?.let { (pb, pH) ->
                return state.calculateTotalHeight(n, pb, pH, reports)
            } ?: let { reports += Triple(state.blockCount.toLong(), state.maxY.toLong(), end) }
        }
        return state.maxY.toLong()
    }

    private fun State.calculateTotalHeight(
        totalBlocks: Long,
        preBlocks: Long,
        preHeight: Long,
        reports: List<Triple<Long, Long, Report>>
    ): Long {
        val (remaining, loopLength, loopHeight) = listOf(totalBlocks - preBlocks, (blockCount - preBlocks), (maxY - preHeight))
        val (loopCount, rem) = listOf(remaining / loopLength, remaining % loopLength)
        val remHeight = if (rem != 0L) reports.find { it.first == preBlocks + rem.toInt() }!!.second - preHeight else 0
        return preHeight + (loopHeight) * loopCount + remHeight
    }

    private data class Report(val cShift: Int, val blockShift: Int, val heightProfile: List<Int>)

    private inner class State(var currents: List<Char>, var s: List<(Point2D)-> Rock> = shapes, ) {
        var maxY = 0
        var cShift = 0
        var blockCount =0
        val rocks =  LinkedHashSet(floor)

        fun dropNext(): Rock {
            var next = s.first()(Point2D(2,maxY +4)).also { s = s.cycle() }
            while(true) {
                next = next.push(currents.first(), rocks).also { currents = currents.cycle() }
                cShift = (cShift +1) % currents.size
                next = next.drop(rocks) ?: return next.also { r ->
                    rocks += r.positions
                    if(rocks.size > 1000) { rocks.removeAll(rocks.take(r.positions.size)) }
                    blockCount ++
                    maxY = maxOf(maxY, r.positions.maxOf { it.y })
                }
            }
        }
        fun heightProfile() = rocks.groupBy({ it.x }, {it.y}).map { it.value.maxOrNull()!! - maxY }
    }

    private fun dash(p: Point2D) =
        Rock(setOf(p, p.right(), p.right().right(), p.right().right().right()))
    private fun cross(p: Point2D) =
        Rock(setOf(p.right(), p.up(), p.up().right(), p.up().right().right(), p.up().up().right()))
    private fun corner(p: Point2D) =
        Rock(setOf(p, p.right(), p.right().right(),p.right().right().up(), p.right().right().up().up(),))
    private fun stick(p: Point2D) =
        Rock(setOf(p, p.up(), p.up().up(), p.up().up().up()))
    private fun block(p: Point2D) =
        Rock(setOf(p, p.up(), p.right(), p.up().right()))
    private val floor = (0..5).runningFold(ORIGIN) { p, _ -> p.right()}.toSet()
    private val shapes = listOf(::dash, ::cross, ::corner, ::stick, ::block)

    override val example = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"
}