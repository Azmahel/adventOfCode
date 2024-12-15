package de.twittgen.aoc.y2024

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.Point2D.Direction
import de.twittgen.aoc.util.Point2D.Direction.*
import de.twittgen.aoc.util.emptyLine
import de.twittgen.aoc.util.toGrid
import de.twittgen.aoc.y2024.Day15.Thing

class Day15: Day<Pair<List<Thing>, List<Direction>>>() {

    override fun String.parse() = split(emptyLine).let { (m,d) ->
        m.toGrid { p, c -> c.toThing(p)} to d.replace("\n","").map { directions[it]!!}
    }

    private fun Char.toThing(position: Point2D) =
        when(this) { 'O' -> Box(listOf(position)) '@' -> Robot(listOf(position)) '#' -> Wall(listOf(position)) else -> null }
    private val directions = mapOf<Char, Direction>( '^' to UP, 'v' to DOWN, '<' to LEFT, '>' to RIGHT)

    init {
        mutableModel=true
        part1(10092, 1497888) { (map, directions) -> map.simulate(directions).toGps() }
        part2(9021, 1522420) { (map, directions) -> map.stretch().simulate(directions).toGps() }
    }

    private fun List<Thing>.stretch() = map { thing -> when(thing) {
        is Wall -> Wall(thing.positions.flatMap { (x,y) -> listOf(Point2D(x*2,y), Point2D(x*2+1, y)) })
        is Box -> Box(thing.positions.flatMap { (x,y) -> listOf(Point2D(x*2,y), Point2D(x*2+1, y)) })
        is Robot -> Robot(thing.positions.map { (x,y) -> Point2D(x*2,y) })
    } }


    private fun List<Thing>.simulate(dir : List<Direction>) =  filterIsInstance<Robot>().let { robots ->
        this.also { dir.forEach { d -> robots.forEach { r -> r.movePushing(d, this) } } }
    }

    private fun List<Thing>.toGps() = maxOf { t -> t.positions[0].y }.let { maxY ->
        filterIsInstance<Box>().sumOf { box -> Point2D(box.positions[0].x, maxY - box.positions[0].y).toGPS() }
    }
    private fun Point2D.toGPS() = 100 * y + x

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

    sealed class Thing(var positions: List<Point2D>)
    private class Wall(positions: List<Point2D>): Thing(positions)
    private sealed class Movable(positions: List<Point2D>): Thing(positions) {
        fun doMove(d: Direction) { positions = positions.map { p -> d.next(p) } }
        private fun next(d: Direction) = positions.map { p -> d.next(p)  }
        private fun collisions(p:List<Point2D>, things: List<Thing>) =
            things.filter { t -> t.positions.any { it in p} && t != this }.toSet()
        fun toPush(d: Direction, things: List<Thing>): Set<Thing> = collisions(next(d), things).let { c ->
            if(c.any { it is Wall }) c else c.flatMap { (it as Movable).toPush(d, things)}.toSet() + c
        }
    }
    private class Robot(positions: List<Point2D>): Movable(positions) {
        fun movePushing(d: Direction, things: List<Thing>) = toPush(d, things).let { push -> if(push.none { it is Wall}) {
            doMove(d).also { push.forEach { p -> (p as Movable).doMove(d)  }}
        } }
    }
    private class Box(positions: List<Point2D>): Movable(positions)
}