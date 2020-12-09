package de.twittgen.aoc.y2020

import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Test

class day5 {
    val input = FileUtil.readInput("2020/day5").lines()
    val examples = listOf(
        "BFFFBBFRRR",
        "FFFBBBFRRR",
        "BBFFBBFRLL"
    )



    @Test
    fun examples() {
        val seats = examples
            .map { getRowDefinition(it) }
            .map { (x,y) ->
                getBinaryNumber(x) to getBinaryNumber(y)
            }
        assert(
            seats == listOf(
                70 to 7,
                14 to 7,
                102 to 4
            )
        )
        val seatnumbers = seats.map { getSeatNumber(it) }
        assert(
            seatnumbers == listOf(
                567,
                119,
                820
            )
        )
    }

    @Test
    fun part1() {
        val seats = input
            .map { getRowDefinition(it) }
            .map { (x,y) ->
                getBinaryNumber(x) to getBinaryNumber(y)
            }.map {
                getSeatNumber(it)
            }
        println(seats.max())
    }

    @Test
    fun part2() {
        val seats = input
            .map { getRowDefinition(it) }
            .map { (x,y) ->
                getBinaryNumber(x) to getBinaryNumber(y)
            }.map {
                getSeatNumber(it)
            }
        val allSeats = (0..128*8)

        println(allSeats.filterNot{it in seats }.filter { it+1 in seats && it-1 in seats } )
    }

    fun getSeatNumber(seat: Pair<Int, Int>, seatsPerRow : Int = 8) =
        seat.first* seatsPerRow + seat.second

    fun getRowDefinition(s: String) = s.partition { it == 'F' || it == 'B' }


    fun getBinaryNumber(input: String, map: Map<Char, Int> = charToBinary): Int
    {
        return input.map { map[it] }.joinToString("").toInt(2)
    }

    private val charToBinary = mapOf('B' to 1, 'F' to 0, 'L' to 0, 'R' to 1)

}