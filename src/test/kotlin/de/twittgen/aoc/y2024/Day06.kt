package de.twittgen.aoc.y2024

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.Point2D.Direction.UP
import de.twittgen.aoc.util.mapCoordinates

class Day06 : Day<Pair<Guard, Room>>() {
    override fun String.parse(): Pair<Guard, Room>  {
        var guard = Guard(Point2D(0,0), UP)
        val (maxX, maxY) = lines().lastIndex to lines()[0].lastIndex
        val walls = mapCoordinates {x,y, c -> when(c) {
                '#' -> Point2D( y, maxX - x)
                '^' -> also { guard = Guard(Point2D(y, maxX - x), UP) }.let { null }
                else -> null
            }}.toSet()
        return guard to Room(walls, 0..maxX to 0..maxY)
    }

    init {
        part1(41, 5404) { (guard, room) ->
            room.traverse(guard).dropLast(1).toSet().size
        }
        part2(6, 1984) { (guard, room) -> room.walkObstructions(guard) }
    }

    private fun Room.traverse(guard: Guard) : List<Point2D> {
        var current = guard
        val positions = emptyList<Guard>().toMutableSet()
        while (current.position inRange boundaries && current !in positions) {
            positions.add(current)
            val next = current.move()
            current = if (next.position in walls) current.turnRight() else next
        }
        return positions.map { it.position } + current.position
    }

    private fun Room.walkObstructions(guard: Guard) =
        traverse(guard).distinct().asSequence().mapNotNull { p ->
            if(p != guard.position && p inRange boundaries )  copy(walls = walls + p).traverse(guard) else null
        }.count { it.last() inRange boundaries }

    private infix fun Point2D.inRange(range: Pair<IntRange, IntRange>) = x in range.first && y in range.second
    override val example = """
        ....#.....
        .........#
        ..........
        ..#.......
        .......#..
        ..........
        .#..^.....
        ........#.
        #.........
        ......#...
    """.trimIndent()

}
data class Guard(val position: Point2D, val facing: Point2D.Direction) {
    fun move() = copy(position = facing.next(position))
    fun turnRight() = copy(facing = facing.turnRight())
}
data class Room(val walls: Set<Point2D>, val boundaries: Pair<IntRange, IntRange>)