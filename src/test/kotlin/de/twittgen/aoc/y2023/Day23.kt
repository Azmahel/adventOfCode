package de.twittgen.aoc.y2023

import de.twittgen.aoc.Day
import de.twittgen.aoc.Day.TestMarker.SLOW
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.toGrid
import de.twittgen.aoc.y2023.Day23.Spot
import java.util.*
import kotlin.math.max

class Day23: Day<Trails>() {
    private val mapping = mapOf('.' to Path, '^' to Up, '<' to Left, '>' to Right, 'v' to Down)
    override fun String.parse() = toGrid { p, c -> mapping[c]?.let { p to it }  }.toMap()

    init {
        part1(94, 2230) { trails ->
            trails.toForkMap().follow()
        }
        part2(154, 6542, SLOW) { trails ->
            trails.mapValues { _ -> Path }.toForkMap().follow()
        }
    }

    private fun ForkMap.follow(): Int {
        val current = Stack<Pair<List<Point2D>, Int>>()
        current.push(listOf(keys.maxBy { it.y }) to 0)
        val end = keys.minBy { it.y }
        var longest = 0
        while (current.isNotEmpty()) {
            current.pop().let { (v, d) ->
                get(v.last())!!.filter{ it.first !in v }.mapNotNull { (n, d2) ->
                    if (n == end) {
                        longest = max(d + d2, longest)
                        null
                    } else {
                        v + n to d + d2
                    }
                }
            }.forEach { current.push(it) }
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

    private fun Trails.findNextFork(
        p: Point2D,
        end: Point2D,
        v: Set<Point2D> = setOf(p),
        d: Int = 0
    ): Pair<Point2D, Int>? {
        return next(p).filter { it !in v }.let {
            when {
            it.size > 1 -> p to d +1
                it.isEmpty() -> null
                else -> if (it.first() == end) end to d + 1 else findNextFork(it.first(), end, v + p, d + 1)
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