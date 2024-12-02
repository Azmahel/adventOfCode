package de.twittgen.aoc.y2023

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.columns
import de.twittgen.aoc.util.mapLines
import de.twittgen.aoc.util.toCharList

class Day14: Day<Platform>() {
    override fun String.parse() = mapLines { it.toCharList() }.rotate()

    init {
        part1(136, 103333) { it.slide().score() }
        part2(64, 97241) {
            it.findLoop().let { (loop, offset) -> loop[((1_000_000_000-offset) % loop.size)] }.score()
        }
    }

    private fun Platform.score() = sumOf { it.mapIndexed { i, c -> if ( c== 'O') i+1 else 0 }.sum() }
    private fun Platform.doCycle() = this
        .slide().rotate() // North
        .slide().rotate() // West
        .slide().rotate() // South
        .slide().rotate() // East

    private fun Platform.findLoop(): Pair<List<Platform>, Int> {
        val stateMap = mutableListOf(this)
        var next =doCycle()
        while (next !in stateMap) { stateMap.add(next).also { next = next.doCycle()  } }
        return stateMap.indexOf(next).let { stateMap.drop(it) to it }
    }

    private fun Platform.rotate() = columns().map { it.reversed() }

    private fun Platform.slide() = map { it.slideToEnd() }

    private fun List<Char>.slideToEnd() : List<Char> {
        val freeEnd = takeLastWhile { it == '.' }
        if (freeEnd == this) return this
        if (freeEnd.isEmpty()) return dropLastWhile { it != '.' }.slideToEnd() + takeLastWhile { it != '.' }
        val pivot = get(lastIndex - freeEnd.size)
        return if (pivot == '#') dropLast(freeEnd.size + 1).slideToEnd() + pivot + freeEnd
        else (dropLast(freeEnd.size + 1) + freeEnd).slideToEnd() + pivot
    }

    override val example = """
        O....#....
        O.OO#....#
        .....##...
        OO.#O....O
        .O.....O#.
        O.#..O.#.#
        ..O..#O..O
        .......O..
        #....###..
        #OO..#....
    """.trimIndent()
}
private typealias  Platform = List<List<Char>>