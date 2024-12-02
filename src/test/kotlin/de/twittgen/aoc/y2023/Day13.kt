package de.twittgen.aoc.y2023

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.*

class Day13 : Day<List<Pattern>>() {
    override fun String.parse() = split(emptyLine).map { it.mapLines { it.toCharList() } }

    init {
        part1(405, 35232) {
            it.sumOf { p -> p.findReflectIndex()*100 + p.columns().findReflectIndex()}
        }
        part2(400, 37982) {
            it.sumOf { p -> p.findSmudgeReflectIndex()*100 + p.columns().findSmudgeReflectIndex() }
        }
    }

    private fun Pattern.findSmudgeReflectIndex() = (1..lastIndex).firstOrNull { n ->
         mirrorAt(n).let { (a,b) -> a.flatMapIndexed { i, l -> l.mapIndexed { j, c -> c != b[i][j] } }.count {it} == 1 }
    } ?: 0

    private fun Pattern.findReflectIndex() = (1..lastIndex).firstOrNull { n ->
        mirrorAt(n).let { (a,b) -> a == b }
    } ?: 0

    private fun Pattern.mirrorAt(n: Int) =
        splitAt(n).let { (a, b) -> minOf(a.size, b.size).let { s -> a.reversed().take(s) to b.take(s) } }

    override val example = """
        #.##..##.
        ..#.##.#.
        ##......#
        ##......#
        ..#.##.#.
        ..##..##.
        #.#.##.#.

        #...##..#
        #....#..#
        ..##..###
        #####.##.
        #####.##.
        ..##..###
        #....#..#
    """.trimIndent()
}
typealias Pattern = List<List<Char>>