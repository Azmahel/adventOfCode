package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.Day.TestState.EXAMPLE
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.groups
import de.twittgen.aoc.util.mapLines
import java.lang.IllegalStateException
import kotlin.math.abs

class Day15: Day<List<Pair<Point2D, Point2D>>>() {
    private val sensorBeaconExpr = Regex("Sensor at x=(.+), y=(.+): closest beacon is at x=(.+), y=(.+)")
    override fun String.parse() = mapLines { l ->
        sensorBeaconExpr.groups(l)!!
            .let { (xS,yS,xB,yB) -> Point2D(xS.toInt(), yS.toInt()) to  Point2D(xB.toInt(), yB.toInt()) }
    }

    init {
        part1(26, 4560025) { readings ->
            readings.mapNotNull { (s, b) -> getBlockedRangeAt(s, b, targetRow()) }.getSize()
        }

        part2(56000011, 12480406634249) { readings ->
            (0..searchLimit()).forEach { y ->
                readings.mapNotNull { (s, b) -> getBlockedRangeAt(s, b, y)?.let { r -> (0..searchLimit()).cut(r) } }
                    .findSingleFree()?.let { x -> return@part2 x * 4000000L + y }
            }
            throw  IllegalStateException()
        }
    }

    private fun List<IntRange>.findSingleFree(): Int? {
        var max =0
        return sortedBy { it.first }
            .find { r-> (r.first > max).also { max = maxOf(max, r.last+1) } }
            ?.first?.minus(1)
    }

    private tailrec fun List<IntRange>.getSize(ranges: Set<Pair<Int,IntRange>> = emptySet()): Int {
        if( this.isEmpty()) return ranges.sumOf { it.first * it.second.size() }
        val next = first()
        val toToggle = ranges.mapNotNull { existing ->
            // see 2021#Day1
            next.cut(existing.second).run { if(!isEmpty()) -existing.first to this else null }
        } + (1 to next)
        return drop(1).getSize(ranges + toToggle)
    }

    private fun IntRange.cut(other: IntRange) = maxOf(first, other.first)..minOf(last,other.last)
    private fun IntRange.size() = abs(last - first)

    private fun getBlockedRangeAt(s: Point2D, b: Point2D, y: Int) = (s.manhattanDistance(b) - abs(s.y - y)).let {
        s.x - it..s.x + it
    }.let { if(it.isEmpty()) null else it }

    private fun targetRow()= if(testState == EXAMPLE) 10 else 2000000
    private fun searchLimit() = if(testState == EXAMPLE) 20 else 4000000

    override val example = """
        Sensor at x=2, y=18: closest beacon is at x=-2, y=15
        Sensor at x=9, y=16: closest beacon is at x=10, y=16
        Sensor at x=13, y=2: closest beacon is at x=15, y=3
        Sensor at x=12, y=14: closest beacon is at x=10, y=16
        Sensor at x=10, y=20: closest beacon is at x=10, y=16
        Sensor at x=14, y=17: closest beacon is at x=10, y=16
        Sensor at x=8, y=7: closest beacon is at x=2, y=10
        Sensor at x=2, y=0: closest beacon is at x=2, y=10
        Sensor at x=0, y=11: closest beacon is at x=2, y=10
        Sensor at x=20, y=14: closest beacon is at x=25, y=17
        Sensor at x=17, y=20: closest beacon is at x=21, y=22
        Sensor at x=16, y=7: closest beacon is at x=15, y=3
        Sensor at x=14, y=3: closest beacon is at x=15, y=3
        Sensor at x=20, y=1: closest beacon is at x=15, y=3
    """.trimIndent()
}