package de.twittgen.aoc.current.y2020

import de.twittgen.aoc.current.Day


class Day05 : Day<List<String>>() {
    override fun String.parse() = lines()
    init {
        part1(820, 880) { it.maxOf { seat ->  seat.binaryNumber() } }
        part2( null, 731) { seats ->
            val seatNumbers = seats.map { seat -> seat.binaryNumber() }
            (0..128*8).filterNot { it in seatNumbers }.first { it + 1 in seatNumbers && it - 1 in seatNumbers }
        }
    }

    private fun String.binaryNumber(map: Map<Char, Char> = charToBinary) =
        map { map[it] }.joinToString("").toInt(2)

    private val charToBinary = mapOf('B' to '1', 'F' to '0', 'L' to '0', 'R' to '1')

    override  val example = """
        BFFFBBFRRR
        FFFBBBFRRR
        BBFFBBFRLL
    """.trimIndent()
}
