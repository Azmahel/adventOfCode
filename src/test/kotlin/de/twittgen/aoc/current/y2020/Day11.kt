package de.twittgen.aoc.current.y2020

import de.twittgen.aoc.current.Day
import de.twittgen.aoc.current.y2020.Day11.TileState
import de.twittgen.aoc.current.y2020.Day11.TileState.*

class Day11 : Day<FloorPlan>() {
    override fun String.parse() = lines().map { it.map { s -> when(s) {
        'L' -> EMPTY
        '#' -> OCCUPIED
        else -> FLOOR
    } } }

    init {
        part1(37, 2412) { fp ->
            fp.runTillStable(getAdjacent, 4).flatten().count { it == OCCUPIED }
        }
        part2(26, 2176) { fp ->
            fp.runTillStable(getVisible, 5).flatten().count { it == OCCUPIED }
        }
    }

    enum class TileState(val value: String) { EMPTY("L"), OCCUPIED("#"), FLOOR(".") }

    private tailrec fun FloorPlan.runTillStable(adj: FloorPlan.(Int, Int)->List<TileState>, seatLimit: Int): FloorPlan {
        val next = next(adj, seatLimit)
        return if(next == this)  this else next.runTillStable(adj, seatLimit)
    }

    fun FloorPlan.next(adj: FloorPlan.(Int, Int)-> List<TileState>, seatLimit: Int) =
        mapIndexed { x, it -> it.mapIndexed { y, seat -> when(seat) {
            EMPTY -> if(adj(x,y).none { it == OCCUPIED }) OCCUPIED else EMPTY
            OCCUPIED -> if(adj(x,y).count { it == OCCUPIED } >= seatLimit ) EMPTY else OCCUPIED
            FLOOR -> FLOOR
        } } }


    private val directions = listOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)

    private val getAdjacent : FloorPlan.(Int, Int) -> List<TileState> = { x, y -> directions.mapNotNull { (dx, dy) ->
        getOrNull(x+ dx)?.getOrNull(y+ dy)
    } }

    private val getVisible : FloorPlan.(Int, Int) -> List<TileState> = { x, y -> directions.mapNotNull { (stepX, stepY) ->
        var ix = x + stepX
        var iy = y + stepY
        while(getOrNull(ix)?.getOrNull(iy) == FLOOR) {
            ix += stepX
            iy +=stepY
        }
        getOrNull(ix)?.getOrNull(iy)
    } }

    override val example = """
        L.LL.LL.LL
        LLLLLLL.LL
        L.L.L..L..
        LLLL.LL.LL
        L.LL.LL.LL
        L.LLLLL.LL
        ..L.L.....
        LLLLLLLLLL
        L.LLLLLL.L
        L.LLLLL.LL
    """.trimIndent()
}
private typealias FloorPlan = List<List<TileState>>
