package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day

class Day6 : Day<Int, Int, String>() {
    override fun String.parse() = this
    override val example = "mjqjpqmgbljsphdztnvjfqwrcgsmlb"

    init {
        part1(7, 1210) {
            findDistinctBlock(4)
        }
        part2(19,) {
            findDistinctBlock(14)
        }
    }

    private fun String.findDistinctBlock(blockSize: Int) = withIndex()
        .windowed(blockSize, 1, true) { block ->
            block.last().index to (block.map { it.value }.distinct().size == blockSize)
        }
        .first { it.second }.first + 1


}