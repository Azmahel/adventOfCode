package de.twittgen.aoc.y2024

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.toGrid
import de.twittgen.aoc.util.toPossiblePairs

class Day08: Day<City>() {
    override fun String.parse(): City = City(
        toGrid { p ,c -> if (Regex("[a-zA-Z0-9]").matches(c.toString())) c to p else null }
            .groupBy { it.first }.mapValues { (_, v) -> v.map { it.second }  },
        Point2D(lines().lastIndex, lines().maxOf(String::lastIndex))
    )

    init {
        part1(14, 271) { (a, max) ->
            a.map { (_, antennas) -> antennas.findAntiNodes() }.reduce { acc, next -> acc + next }.count { it.isInMap(max) }
        }
        part2(34, 994) { (a, max) ->
            a.map { (_, antennas) -> antennas.findHarmonics(max) }.reduce { acc, next -> acc + next }.count()
        }
    }

    private fun Point2D.isInMap(max: Point2D) = x in 0..max.x && y in 0..max.y
    private fun List<Point2D>.findAntiNodes() = forAllPossiblePairs { (a,b) ->  findAntiNodes(a, b) }
    private fun findAntiNodes(a: Point2D, b: Point2D) = (b - a).let { setOf(a - it, a + (it * 2)) }
    private fun List<Point2D>.findHarmonics(max: Point2D) = forAllPossiblePairs { (a,b) -> findHarmonics(a, b, max) }
    private fun List<Point2D>.forAllPossiblePairs(block: (Pair<Point2D, Point2D>) -> Set<Point2D>) =
        toPossiblePairs().map(block).reduce { acc, next -> acc + next}
    private fun findHarmonics(a: Point2D, b: Point2D, max: Point2D)=
        (b -a).let { d -> a.harmonicsTo(max) { this - d } + a.harmonicsTo(max) { this + d } }
    private fun Point2D.harmonicsTo(max: Point2D, dir: Point2D.() -> Point2D): Set<Point2D> =
        if(!isInMap(max)) emptySet() else setOf(this) + dir().harmonicsTo(max, dir)

    override val example = """
       ............
       ........0...
       .....0......
       .......0....
       ....0.......
       ......A.....
       ............
       ............
       ........A...
       .........A..
       ............
       ............
    """.trimIndent()
}
private typealias City = Pair<Map<Char, List<Point2D>>, Point2D>