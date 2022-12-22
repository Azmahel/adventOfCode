package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.y2022.Day22.*
import de.twittgen.aoc.y2022.Day22.Facing.*
import de.twittgen.aoc.y2022.Day22.Tile.*
import java.lang.IllegalStateException



class Day22: Day<Pair<Board,List<Instruction>>>() {

    override fun String.parse() = split("\n\n").let { (map, instructions) ->
        parseMap(map) to parseInstructions(instructions)
    }

    private fun parseMap(s: String) =s.lines().flatMapIndexed { row, l -> l.mapIndexed { column, c ->
        Point2D(column +1, row +1) to when(c) {
            ' ' -> VOID
            '.' -> OPEN
            '#' -> WALL
            else -> throw IllegalStateException()
        } } }.toMap().withDefault { VOID }

    private fun parseInstructions(s: String) : List<Instruction> {
        val result = mutableListOf<Instruction>()
        var remainder = s
        while (remainder.isNotEmpty()) {
            with(remainder.first()) { when {
                isDigit() -> result +=Distance(remainder.takeWhile { it.isDigit() }.toInt())
                    .also { remainder = remainder.dropWhile { it.isDigit() } }
                else -> result += when(this) {
                    'L' -> Left
                    'R' -> Right
                    else -> throw IllegalStateException()
                }.also { remainder = remainder.drop(1) }
            }}
        }
        return result
    }

    init {
        part1(6032, 56372) { (board, instructions) -> State(board, instructions).run().score() }
    }

    data class State(
        val board: Board,
        val ins: List<Instruction>,
        var pointer: Pair<Point2D, Facing> =
            board.filterValues { it == OPEN }.keys.filter { it.y == 1 }.minByOrNull { it.x }!! to RIGHT,
    ) {
        private val remaining = ins.toMutableList()
        fun run(): State {
            while (remaining.isNotEmpty()) doStep()
            return this
        }
        fun score() = 1000 * pointer.first.y + 4* pointer.first.x + pointer.second.ordinal
        private fun doStep() = with(remaining.removeFirst()) {
            pointer = when(this) {
                is Direction -> pointer.first to pointer.second.turn(this)
                is Distance -> board.move (pointer.first, pointer.second, length ) to pointer.second
            }
        }.let { this }
        private fun Board.move(position: Point2D, facing: Facing, length: Int) : Point2D {
            var current = position
            repeat(length) {
                val next = facing.next(current)
                current = when(get(next)) {
                    OPEN -> next
                    WALL -> return current
                    else -> {
                        val n = wrap(position, facing)
                        when(get(n)!!) {
                            OPEN -> n
                            WALL -> return current
                            VOID -> throw IllegalStateException()
                        }
                    }

                }
            }
            return current
        }
        private fun Board.wrap(position:Point2D, facing: Facing) =  when (facing) {
            UP -> filterKeys { it.x == position.x }.filterValues { it != VOID }.keys.maxByOrNull { it.y }!!
            DOWN -> filterKeys { it.x == position.x }.filterValues { it != VOID }.keys.minByOrNull { it.y }!!
            LEFT -> filterKeys { it.y == position.y }.filterValues { it != VOID }.keys.maxByOrNull { it.x }!!
            RIGHT -> filterKeys { it.y == position.y }.filterValues { it != VOID }.keys.minByOrNull { it.x }!!
        }
    }


    sealed class Instruction
    sealed class Direction : Instruction()
    class Distance(val length : Int): Instruction()
    object Right: Direction()
    object Left:  Direction()
    enum class Facing(val next: (Point2D) -> Point2D,) {
        RIGHT(Point2D::right), DOWN(Point2D::up),LEFT(Point2D::left), UP(Point2D::down); // positive y is down
        fun turn(d: Direction) = when(d) {
            Left  -> values().first{ it.ordinal == (ordinal - 1).mod(values().size) }
            Right -> values().first{ it.ordinal == (ordinal + 1).mod(values().size) }
        }
    }
    enum class Tile { VOID, OPEN, WALL }

    override val example = """
            ...#
            .#..
            #...
            ....
    ...#.......#
    ........#...
    ..#....#....
    ..........#.
            ...#....
            .....#..
            .#......
            ......#.
    
    10R5L5R10L4R5L5
    """.trimIndent()
}


private typealias Board = Map<Point2D, Tile>

