package de.twittgen.aoc.y2020

import de.twittgen.aoc.y2019.shared.util.FileUtil
import de.twittgen.aoc.y2020.day11.TileState.*
import org.junit.jupiter.api.Test
import java.util.*

typealias Floorplan = List<List<day11.TileState>>
class day11 {
    val input = FileUtil.readInput("2020/day11")
    val example = """
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

    enum class TileState(val value: String) {
        EMPTY("L"),
        OCCUPIED("#"),
        FLOOR(".")
    }

    fun parseInput(s: String): Floorplan {
        return s.lines().map {
            it.map {
                when(it) {
                    'L' -> EMPTY
                    '#' -> OCCUPIED
                    else -> FLOOR
                }
            }
        }
    }

    @Test
    fun example() {
        val map = parseInput(example)
        val final = runTillStable(map)
        assert(
            final.map { it.count { it == OCCUPIED } }.sum() == 37
        )
    }

    @Test
    fun part1() {
        val map = parseInput(input)
        val final = runTillStable(map)
        println(
            final.map { it.count { it == OCCUPIED } }.sum()
        )
    }

    @Test
    fun part2() {
        val map = parseInput(input)
        val final = runTillStable(map, getVisiblyAdjacent, 5)
        println(
            final.map { it.count { it == OCCUPIED } }.sum()
        )
    }

    fun runTillStable(plan: Floorplan, adj: Floorplan.(Int,Int)-> List<TileState> = getDirectlyAdjacent, seatLimit: Int = 4): Floorplan {
        var current = plan
        var next = plan.next(adj, seatLimit)
        while(next != current) {
            current = next
            next= current.next(adj, seatLimit)
        }
        return current
    }

    fun Floorplan.next(adj: Floorplan.(Int,Int)-> List<TileState>, seatLimit: Int) : Floorplan {
        return mapIndexed { x, it ->
            it.mapIndexed { y, seat ->
                when(seat) {
                    EMPTY -> if(adj(x,y).none { it == OCCUPIED }) OCCUPIED else EMPTY
                    OCCUPIED -> if(adj(x,y).count { it == OCCUPIED } >= seatLimit ) EMPTY else OCCUPIED
                    FLOOR -> FLOOR
                }
            }
        }
    }

    val directions = listOf(
        -1 to -1,
        -1 to 0,
        -1 to 1,
        0 to -1,
        0 to 1,
        1 to -1,
        1 to 0,
        1 to 1
    )

    val getDirectlyAdjacent : Floorplan.(Int, Int) -> List<TileState> = { x, y ->
        directions.mapNotNull { (dx, dy) ->
            getOrNull(x+ dx)?.getOrNull(y+ dy)
        }
    }



    val getVisiblyAdjacent : Floorplan.(Int, Int) -> List<TileState> = { x, y ->
        val result = directions.mapNotNull { (stepX, stepY) ->
            var ix = x + stepX
            var iy = y + stepY
            while(getOrNull(ix)?.getOrNull(iy) == FLOOR) {
                ix += stepX
                iy +=stepY
            }
            getOrNull(ix)?.getOrNull(iy)
        }
        result
    }

}