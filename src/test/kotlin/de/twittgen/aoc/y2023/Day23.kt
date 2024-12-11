package de.twittgen.aoc.y2023

import de.twittgen.aoc.Day
import de.twittgen.aoc.Day.TestMarker.SLOW
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.toGrid
import de.twittgen.aoc.y2023.Day23.Spot
import kotlin.math.max

class Day23: Day<Trails>() {
    private val mapping = mapOf('.' to Path, '^' to Up, '<' to Left, '>' to Right, 'v' to Down)
    override fun String.parse() = toGrid { p, c -> mapping[c]?.let { p to it }  }.toMap()

    init {
        part1(94, 2230) { trails ->
            trails.toForkMap().follow()
        }
        part2(154, 2230) { trails ->
            trails.mapValues { _ -> Path }.toForkMap().follow()
        }
    }

    private fun ForkMap.follow(
        current: Point2D = keys.maxBy { it.y } ,
        end: Point2D = keys.minBy { it.y },
        visited: Set<Point2D> = emptySet(),
        distance: Int = 0
    ): Int  {
        if (current == end) return distance + 1
        return get(current)!!.maxOf { (p, d) -> follow(p, end, visited + p, distance + d) }
    }

    private fun ForkMap.follow(): Int {
        var current = setOf(Triple(keys.maxBy { it.y } ,0,emptySet<Point2D>()))
        val end = keys.minBy { it.y }
        var longest = 0
        while (current.isNotEmpty()) {
            current = current.flatMap { (p, d, v) ->
                get(p)!!
                    .filter { it.first !in v }
                    .map { (n, d2) ->
                        Triple(n, d + d2, v + p ).also { (p,d) -> if (p == end) longest = max(longest,d) }
                    }
            }.toSet()
        }
        return longest + 1
    }

    private fun Trails.toForkMap() : ForkMap {
        var current = setOf(keys.maxBy { it.y })
        val target = keys.minBy { it.y }
        val map = mutableMapOf<Point2D, Set<Pair<Point2D, Int>>>()
        while (current.isNotEmpty()) {
            current = current.flatMap{ p ->
                next(p).filter { it !in map }
                    .mapNotNull { findNextFork(it, target, setOf(p))}
                    .toSet()
                    .also { map[p] = it }
                    .filter { it.first !in map }
            }.map { it.first }.toSet()
        }
        return map
    }

    private fun Trails.next(p: Point2D) = get(p)!!.next(p).filter { contains(it) }

    private fun Trails.findNextFork(p: Point2D, end :Point2D, visited: Set<Point2D> = setOf(p), d: Int = 0) : Pair<Point2D, Int>? {
        return next(p).filter { it !in visited }. let { when {
            it.size > 1 -> p to d +1
            it.size == 1 -> if(it.first() == end) end to d +1  else  findNextFork(it.first(), end,visited + p, d + 1)
            else -> null
        } }
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
typealias ForkMap = Map<Point2D, Set<Pair<Point2D, Int>>>