package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.Day.TestMarker.HACKY
import de.twittgen.aoc.Day.TestState.EXAMPLE
import de.twittgen.aoc.util.Point2D
import de.twittgen.aoc.util.emptyLine
import de.twittgen.aoc.y2022.Day22.*
import de.twittgen.aoc.y2022.Day22.Facing.*
import de.twittgen.aoc.y2022.Day22.Tile.*
import java.lang.IllegalStateException

class Day22: Day<Pair<Board,List<Instruction>>>() {

    override fun String.parse() = split(emptyLine).let { (map, instructions) ->
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
        part2(5031, 197047, HACKY) { (board, instructions) ->
            State(board, instructions).run(true).score() }
    }

    private inner class State(
        val board: Board,
        ins: List<Instruction>,
        var pointer: Pair<Point2D, Facing> =
            board.filterValues { it == OPEN }.keys.filter { it.y == 1 }.minByOrNull { it.x }!! to RIGHT,
    ) {
        private val remaining = ins.toMutableList()

        fun run(cubes: Boolean = false): State {
            while (remaining.isNotEmpty()) doStep(cubes)
            return this
        }

        fun score() =
            1000 * pointer.first.y + 4* pointer.first.x + pointer.second.ordinal

        private fun doStep(cubes: Boolean = false) = with(remaining.removeFirst())  {
            pointer = when(this) {
                is Direction -> pointer.first to pointer.second.turn(this)
                is Distance -> board.move (pointer.first, pointer.second, length , cubes)
            }
        }.let { this }

        private fun Board.move(position: Point2D, facing: Facing, length: Int, cubes: Boolean = false) : Pair<Point2D, Facing> {
            val wrapFunc : (Pair<Point2D, Facing>) -> Pair<Point2D, Facing> =
                if(cubes) { p -> wrapCubes(p) } else {(p,f) ->  this.wrap(p,f)}
            var current = position to facing
            repeat(length) {
                val next = current.second.next(current.first)
                current = when(get(next)) {
                    OPEN -> next to current.second
                    WALL -> return current
                    else -> {
                        val n = wrapFunc(current)
                        when(get(n.first)!!) {
                            OPEN -> n
                            WALL -> return current
                            VOID -> throw IllegalStateException()
                        }
                    }
                }
            }
            return current
        }

        private fun wrapCubes(pointer: Pair<Point2D, Facing>) : Pair<Point2D, Facing> {
            val x = (pointer.first.x -1) / sideLength()
            val y = (pointer.first.y -1) / sideLength()
            return if (testState == EXAMPLE) { exampleWrapping[y to x]!!(pointer)} else { realWrapping[y to x]!!(pointer)}
        }

        private fun Board.wrap(position:Point2D, facing: Facing) =  when (facing) {
            UP    -> filterKeys { it.x == position.x }.filterValues { it != VOID }.keys.maxByOrNull { it.y }!! to facing
            DOWN  -> filterKeys { it.x == position.x }.filterValues { it != VOID }.keys.minByOrNull { it.y }!! to facing
            LEFT  -> filterKeys { it.y == position.y }.filterValues { it != VOID }.keys.maxByOrNull { it.x }!! to facing
            RIGHT -> filterKeys { it.y == position.y }.filterValues { it != VOID }.keys.minByOrNull { it.x }!! to facing
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

    private fun sideLength() = if (testState == EXAMPLE) 4 else 50

    //wrappings done by folding nets by hand
    private val exampleWrapping : Map<Pair<Int,Int>, (Pair<Point2D, Facing>) -> Pair<Point2D, Facing>> = mapOf(
        (0 to 2) to {(p, f) -> when(f) {
            UP -> Point2D(13-p.x, 5) to DOWN
            LEFT -> Point2D( p.y + 4, 5) to DOWN
            RIGHT -> Point2D( 16, 13-p.y ) to DOWN
            else -> throw IllegalStateException()
        } },
        (1 to 0) to {(p, f) -> when(f) {
            UP -> Point2D(13-p.x, 1) to DOWN
            DOWN -> Point2D(p.y, 12) to UP
            LEFT -> Point2D(21- p.x    , 12) to UP
            else -> throw IllegalStateException()
        } },
        (1 to 1) to {(p, f) -> when(f) {
            UP -> Point2D( 9,p.x-4) to RIGHT
            DOWN -> Point2D(9, 17- p.x ) to RIGHT
            else -> throw IllegalStateException()
        } },
        (1 to 2) to {(p, f) -> when(f) {
            RIGHT -> Point2D( 21-p.y, 9) to DOWN
            else -> throw IllegalStateException()
        } },
        (2 to 2) to {(p, f) -> when(f) {
            LEFT -> Point2D(17-p.y , 8)to UP
            DOWN -> Point2D(13-p.x,8) to UP
            else -> throw IllegalStateException()
        } },
        (2 to 3) to {(p, f) -> when(f) {
            UP -> Point2D(12, 21-p.x) to LEFT
            DOWN -> Point2D(1, 21-p.x) to RIGHT
            RIGHT -> Point2D(12, 13-p.y) to LEFT
            else -> throw IllegalStateException()
        } }
    )
    private val realWrapping : Map<Pair<Int,Int>, (Pair<Point2D, Facing>) -> Pair<Point2D, Facing>> = mapOf(
        (0 to 1) to { (p, f) -> when(f) {
            UP -> Point2D(1, p.x + 100) to RIGHT
            LEFT -> Point2D( 1, 151-p.y) to RIGHT
            else -> throw IllegalStateException()
        } },
        (0 to 2) to {(p,f) -> when(f) {
            UP -> Point2D(p.x-100, 200) to UP
            RIGHT -> Point2D(100, 151-p.y) to LEFT
            DOWN -> Point2D(100, p.x-50) to LEFT
            else -> throw IllegalStateException()
        } },
        (1 to 1) to {(p,f) -> when(f) {
            LEFT -> Point2D(p.y-50, 101) to DOWN
            RIGHT -> Point2D(p.y + 50,50) to UP
            else -> throw IllegalStateException()
        } },
        (2 to 0) to {(p,f) -> when(f) {
            UP -> Point2D(51, p.x +50) to RIGHT
            LEFT -> Point2D(51, 151-p.y) to RIGHT
            else -> throw IllegalStateException()
        } },
        (2 to 1) to {(p,f) -> when(f){
            RIGHT -> Point2D(150,151-p.y) to LEFT
            DOWN -> Point2D(50 , p.x +100) to LEFT
            else -> throw IllegalStateException()
        } },
        (3 to 0) to {(p,f)-> when(f) {
            LEFT -> Point2D(p.y -100, 1) to DOWN
            DOWN -> Point2D(p.x+100, 1) to DOWN
            RIGHT -> Point2D(p.y -100,150) to UP
            else -> throw IllegalStateException()
        } }
    )

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

