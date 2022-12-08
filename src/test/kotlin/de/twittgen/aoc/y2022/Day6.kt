package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day

class Day6 : Day<Int, Int, String>() {
    override fun String.parse() = this

    init {
        part1(7, 1210) { findDistinctBlock(4) }
        part2(19, 3476) { findDistinctBlock(14) }
    }

    private fun String.findDistinctBlock(blockSize: Int) =
        windowed(blockSize).indexOfFirst { it.toSet().size == blockSize } + blockSize

    override val example = "mjqjpqmgbljsphdztnvjfqwrcgsmlb"
}