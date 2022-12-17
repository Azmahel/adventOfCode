package de.twittgen.aoc.current.y2022

import de.twittgen.aoc.current.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.alphabet

class Day12: Day<Pair<Point2D, PathMap>>() {
    override fun String.parse(): Pair<Point2D, PathMap> {
        var target : Point2D = Point2D.ORIGIN
        val map = lines().flatMapIndexed { x, l -> l.mapIndexed { y, c ->
            Point2D(x,y)  to when(c) {
                'S' -> alphabet.indexOf('a') to 0
                'E' -> (alphabet.indexOf('z') to Int.MAX_VALUE).also { target = Point2D(x,y) }
                else -> alphabet.indexOf(c) to Int.MAX_VALUE
            }
        } }.toMap()
        return target to map
    }

    init {
        part1(31,423) { (target, map ) -> map.findPath(setOf(target)) }
        part2(29, 416) { (target, map) -> map.mapValues { (p, d) -> d.let{ (ele, _) ->
            (alphabet.indexOf('z') - ele) to (if (p== target) 0 else Int.MAX_VALUE)
        } }.findPath(map.filter { (_,d) -> d.first == alphabet.indexOf('a') }.keys) }
    }

    private tailrec fun PathMap.findPath(targets: Set<Point2D>, currentStep : Int = 0): Int {
        val startingPoints = filterValues { it.second == currentStep }.keys
        val next = startingPoints
            .flatMap { point ->
                point.orthogonallyAdjacent()
                .filter { it in this }
                .filter { this[it]!!.let { (nextEle, nextDist) -> this[point]!!.let { (ele, dist) ->
                        nextEle <= ele + 1 && nextDist > dist
                } } }
            }
        if(targets.any {  it in next } ) return currentStep +1
        if(next.isEmpty()) return Int.MAX_VALUE

        return mapValues { (point, d) -> d.let { (ele, dist) ->
            if(point in next) ele to (currentStep +1) else ele to dist
        }}.findPath(targets, currentStep +1)
    }

    override val example = """
        Sabqponm
        abcryxxl
        accszExk
        acctuvwj
        abdefghi
    """.trimIndent()
}
typealias PathMap = Map<Point2D, Pair<Int, Int>>