package de.twittgen.aoc.y2023

import de.twittgen.aoc.Day
import de.twittgen.aoc.Day.TestMarker.SLOW
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.toGrid
import de.twittgen.aoc.y2023.Day23.Spot

class Day23: Day<Trails>() {
    private val mapping = mapOf('.' to Path, '^' to Up, '<' to Left, '>' to Right, 'v' to Down)
    override fun String.parse() = toGrid { p , c -> mapping[c]?.let { p to it }  }.toMap()

    init {
        part1(94, 2230, SLOW) {
            it.follow()
        }
    }

    private fun Trails.follow(
        current: Point2D= keys.maxBy { it.y }, end: Point2D = keys.minBy {it.y}, visited: Set<Point2D> = emptySet()
    ): List<Set<Point2D>> {
        if (current == end) return listOf(visited)
        return get(current)!!.next(current)
            .filter { contains(it) && it !in visited }
            .flatMap { follow(it, end, visited + it ) }
    }



    override val example = """
        #.#####################
        #.......#########...###
        #######.#########.#.###
        ###.....#.>.>.###.#.###
        ###v#####.#v#.###.#.###
        ###.>...#.#.#.....#...#
        ###v###.#.#.#########.#
        ###...#.#.#.......#...#
        #####.#.#.#######.#.###
        #.....#.#.#.......#...#
        #.#####.#.#.#########v#
        #.#...#...#...###...>.#
        #.#.#v#######v###.###v#
        #...#.>.#...>.>.#.###.#
        #####v#.#.###v#.#.###.#
        #.....#...#...#.#.#...#
        #.#########.###.#.#.###
        #...###...#...#...#.###
        ###.###.#.###v#####v###
        #...#...#.#.>.>.#.>.###
        #.###.###.#.###.#.#v###
        #.....###...###...#...#
        #####################.#
    """.trimIndent()

    sealed class Spot { abstract fun next(p: Point2D): List<Point2D> }
    private data object Path : Spot() { override fun next(p: Point2D) = p.orthogonallyAdjacent() }
    private sealed class Slope(val d: Point2D.()->Point2D) : Spot() { override fun next(p: Point2D) = listOf(p.d()) }
    private data object Up: Slope(Point2D::up)
    private data object Down: Slope(Point2D::down)
    private data object Left: Slope(Point2D::left)
    private data object Right: Slope(Point2D::right)
}
typealias Trails = Map<Point2D, Spot>