package de.twittgen.aoc.y2024

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.Point2D.Direction.UP
import de.twittgen.aoc.util.toGrid

class Day06 : Day<Pair<Guard, Room>>() {
    override fun String.parse(): Pair<Guard, Room>  {
        var guard = Guard(Point2D(0,0), UP)
        val walls = toGrid { p, c-> when(c) {
                '#' -> p
                '^' -> null.also { guard = Guard(p, UP) }
                else -> null
            }}.toSet()
        return guard to Room(walls, 0..lines().lastIndex to 0..lines().maxOf(String::lastIndex))
    }

    init {
        part1(41, 5404) { (guard, room) -> room.traverse(guard).dropLast(1).toSet().size }
        part2(6, 1984) { (guard, room) -> room.walkObstructions(guard) }
    }

    private fun Room.traverse(guard: Guard) : List<Point2D> {
        var (current, positions) = guard to emptyList<Guard>().toMutableSet()
        while (current.position inSide boundaries && current !in positions) {
            positions.add(current)
            current = current.move().let { if (it.position in walls) current.turnRight() else it }
        }
        return positions.map(Guard::position) + current.position
    }

    private fun Room.walkObstructions(guard: Guard) =
        traverse(guard).distinct().asSequence().mapNotNull { p ->
            if (p != guard.position && p inSide boundaries) copy(walls = walls + p).traverse(guard) else null
        }.count { it.last() inSide boundaries }

    private infix fun Point2D.inSide(range: Pair<IntRange, IntRange>) = x in range.first && y in range.second
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