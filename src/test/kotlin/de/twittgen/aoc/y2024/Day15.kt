package de.twittgen.aoc.y2024

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.Point2D.Direction.*
import de.twittgen.aoc.util.emptyLine
import de.twittgen.aoc.util.toGrid
import de.twittgen.aoc.y2024.Day15.Thing

class Day15: Day<Pair<List<Thing>, List<Point2D.Direction>>>() {

    override fun String.parse() = split(emptyLine).let { (m,d) ->
        m.toGrid { p, c -> c.toThing(p)} to d.replace("\n","").map { directions[it]!!}
    }

    private fun Char.toThing(position: Point2D) =
        when(this) { 'O' -> Box(position) '@' -> Robot(position) '#' -> Wall(position) else -> null }
    private val directions = mapOf<Char, Point2D.Direction>( '^' to UP, 'v' to DOWN, '<' to LEFT, '>' to RIGHT)

    init {
        mutableModel=true
        part1(10092) { (map, directions) ->
            map.filterIsInstance<Robot>().let { robots -> directions.forEach { d ->
                robots.forEach { r -> r.move(d, map) }
            }}
            map.toGps()
        }
    }

    private fun List<Thing>.toGps() = maxOf { t -> t.position.y }.let { maxY -> filterIsInstance<Box>().sumOf { box -> Point2D(box.position.x, maxY - box.position.y).toGPS()  } }
    private fun Point2D.toGPS() = 100*y + x
    private fun List<Thing>.printMap() = associateBy { it.position}.printMap()
    private fun Map<Point2D,Thing>.printMap() =
        (keys.maxOf { it.y  } downTo 0).joinToString("\n") { y -> (0..keys.maxOf {it.x}).joinToString("") { x ->
            get(Point2D(x,y))?.let { t -> when(t) { is Wall -> "#"; is Robot -> "@"; is Box -> "O" } } ?: "."
        } }

    override val example = """
        ##########
        #..O..O.O#
        #......O.#
        #.OO..O.O#
        #..O@..O.#
        #O#..O...#
        #O..O..O.#
        #.OO.O.OO#
        #....O...#
        ##########

        <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
        vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
        ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
        <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
        ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
        ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
        >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
        <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
        ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
        v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
    """.trimIndent()
    sealed class Thing(var position: Point2D) {
        abstract fun move(d: Point2D.Direction, places: List<Thing>) : Boolean
    }
    private class Wall(position: Point2D): Thing(position) { override fun move(d: Point2D.Direction, places: List<Thing>) = false }
    private sealed class Movable(position: Point2D): Thing(position) {
        override fun move(d: Point2D.Direction, places: List<Thing>) = d.next(position).let { next ->
            (places.find { p -> p.position == next }?.move(d, places) != false).also{ if(it) position = next }
        }
    }
    private class Robot(position: Point2D): Movable(position)
    private class Box(position: Point2D): Movable(position)
}