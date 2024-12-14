package de.twittgen.aoc.y2024

import de.twittgen.aoc.Day
import de.twittgen.aoc.Day.TestState.EXAMPLE
import de.twittgen.aoc.Day.TestState.REAL
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.mapLines
import de.twittgen.aoc.util.variance

class Day14: Day<List<Robot>>() {
    val robotRegex = Regex("p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)")
    override fun String.parse() = mapLines { r -> robotRegex.matchEntire(r)!!.groupValues.let { (_, a, b, c, d) ->
        Point2D(a.toInt(),b.toInt()) to Point2D(c.toInt(),d.toInt())
    } }

    init {
        part1(12, 222901875) {
          it.simulate().quadrants().let { map -> map[1]!!.size * map[2]!!.size * map[3]!!.size * map[4]!!.size }
        }
        part2(null, 6243) {
            (0..it.toLoop()).asSequence()
                .runningFold((0.0 to 0.0) to (0.0 to 0.0)) { (m,_), i -> it.simulate(i).meanDeviation() to m }
                .indexOfFirst { (m1, m2) ->  m1.first < m2.first * sig && m1.second < m2.second * sig } - 1
        }
    }
    private val sig = 0.55

    private fun List<Robot>.simulate(i:Int = 100)= map { it.simulate(i).first }
    private fun List<Point2D>.quadrants() = groupBy { when {
        it.x < maximum().x / 2  && it.y < maximum().y / 2 -> 1
        it.x < maximum().x / 2  && it.y > maximum().y / 2 -> 2
        it.x > maximum().x / 2  && it.y < maximum().y / 2 -> 3
        it.x > maximum().x / 2  && it.y > maximum().y / 2 -> 4
        else -> 0
    } }

    private fun maximum() = when(testState) {EXAMPLE -> Point2D(11, 7); REAL -> Point2D(101, 103) }
    private fun Robot.simulate(steps: Int = 100) = ((first + (second * steps)) % maximum()).modWrap(maximum()) to second
    private fun List<Point2D>.meanDeviation() = map { it.x }.variance()  to map { it.y }.variance()
    private fun List<Robot>.toLoop() = (0..Int.MAX_VALUE).asSequence().first { i -> map { r -> r.simulate(i) } == this }
    override val example = """
        p=0,4 v=3,-3
        p=6,3 v=-1,-3
        p=10,3 v=-1,2
        p=2,0 v=2,-1
        p=0,0 v=1,3
        p=3,0 v=-2,-2
        p=7,6 v=-1,-3
        p=3,0 v=-1,-2
        p=9,3 v=2,3
        p=7,3 v=-1,2
        p=2,4 v=2,-3
        p=9,5 v=-3,-3
    """.trimIndent()
}
private typealias Robot = Pair<Point2D, Point2D>
