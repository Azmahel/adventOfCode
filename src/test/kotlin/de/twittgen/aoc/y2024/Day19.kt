package de.twittgen.aoc.y2024

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.emptyLine

class Day19 : Day<Pair<Regex,List<String>>>() {
    override fun String.parse() = split(emptyLine).let { (r, l) -> toRegex(r) to l.lines()  }
    private fun toRegex(s : String) = Regex("(${s.split(", ").joinToString("|")})+")

    init {
        part1(6,) { (r, l) -> l.count { r.matches(it) } }
    }

    override val example = """
        r, wr, b, g, bwu, rb, gb, br

        brwrr
        bggr
        gbbr
        rrbgbr
        ubwu
        bwurrg
        brgr
        bbrgwb
    """.trimIndent()
}