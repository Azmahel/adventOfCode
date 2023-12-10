package de.twittgen.aoc.y2023

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.mapCoordinates
import de.twittgen.aoc.util.second

class Day10: Day<Pair<Point2D, Map<Point2D, PipeSegment>>>() {
    override fun String.parse(): Pair<Point2D, Map<Point2D,PipeSegment>> {
        var start = Point2D.ORIGIN
        val segments = lines().reversed().joinToString("\n").mapCoordinates { y, x, c -> with(Point2D(x,y)) {
            this to mapping[c]!!().also { if (c == 'S') start = this }
        }}.toMap()
        return start to segments
    }

    private val mapping = mapOf<Char,Point2D.() -> Set<Point2D>>(
        '|' to   {setOf(north() , south())},
        '-' to { setOf(east(), west()) },
        'L' to { setOf(north(), east()) },
        'J' to { setOf(north(), west()) },
        '7' to { setOf(south(), west()) },
        'F' to { setOf(south(), east()) },
        'S' to { setOf(north(), east(), west(), south()) },
        '.' to { emptySet() },
        )

    init {
        part1(80,6697) {(start, map) -> start.findLoop(map).size/2 }
        part2(10,  423) {(start, map) ->
            start.findLoop(map)
                .toDiagram(start)
                .map {  it.replace(pipeExp, "|") }
                .sumOf { l -> l.findEnclosedByParity() }
        }
    }

    private fun String.findEnclosedByParity() =
        mapIndexed { i, c -> if (c == '.' && take(i).count { it == '|' } % 2 == 1) 1 else 0 }.sum()

    private fun List<Point2D>.toDiagram(start: Point2D)= raw!!
        .replace('S', start.mapStartToPipe(this))
        .lines().reversed().mapIndexed { y, l ->
            l.mapIndexed { x, c -> if (Point2D(x, y) in this) c else '.' }.joinToString("")
        }

    private val pipeExp = Regex("(L-*7)|(F-*J)")

    private fun Point2D.mapStartToPipe(loop: List<Point2D> ) = when(setOf(loop.second(), loop.last())) {
        setOf(north(), south()) -> '|'
        setOf(east(), west()) -> '-'
        setOf(south(), east()) -> 'F'
        setOf(south(), west()) -> '7'
        setOf(north(), east()) -> 'L'
        setOf(north(), west()) -> 'J'
        else -> throw  IllegalArgumentException()
    }

    private fun Point2D.findLoop(map: Map<Point2D, PipeSegment>) =
        getAdjacentOnPipe(map).asSequence().map { listOf(this, it).find(this, map) }.first { it.isNotEmpty() }

    private fun Point2D.getAdjacentOnPipe(map: Map<Point2D, PipeSegment>) =
        map[this]!!.filter { map[it]?.contains(this) ?: false }

    private tailrec fun List<Point2D>.find(target: Point2D, map: Map<Point2D, PipeSegment>) : List<Point2D> {
        val next = map[last()]
            ?.firstOrNull { it != get(size - 2) }
            ?.let { if(last() in map[it]!!) it else null }
            ?: return emptyList()
        return if(next == target) this else (this + next).find(target, map)
    }


    override val example = """
        FF7FSF7F7F7F7F7F---7
        L|LJ||||||||||||F--J
        FL-7LJLJ||||||LJL-77
        F--JF--7||LJLJ7F7FJ-
        L---JF-JLJ.||-FJLJJ7
        |F|F-JF---7F7-L7L|7|
        |FFJF7L7F-JF7|JL---7
        7-L-JL7||F7|L7F-7F7|
        L.L7LFJ|||||FJL7||LJ
        L7JLJL-JLJLJL--JLJ.L
    """.trimIndent()
}


private typealias PipeSegment = Set<Point2D>