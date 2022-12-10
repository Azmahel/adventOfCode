package de.twittgen.aoc.current.y2022

import de.twittgen.aoc.Day

class Day6 : Day<String>() {
    override fun String.parse() = this

    init {
        part1(7, 1210) { it.findDistinctBlock(4) }
        part2(19, 3476) { it.findDistinctBlock(14) }
    }

    private fun String.findDistinctBlock(blockSize: Int) =
        windowed(blockSize).indexOfFirst { it.toSet().size == blockSize } + blockSize

    override val example = "mjqjpqmgbljsphdztnvjfqwrcgsmlb"
}