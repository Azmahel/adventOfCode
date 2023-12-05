package de.twittgen.aoc.y2023

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.second
import kotlin.math.pow

class Day04: Day<List<Int>>() {
    override fun String.parse() = lines().map { l ->
        l.split(": ").second().split(" | ").let { (a,b) -> a.toScratchCard() to b.toScratchCard() }
    }.map { (a,b) -> a.intersect(b).size }

    init {
        part1(13, 24848) { cards ->
            cards.sumOf { s -> if(s ==0 ) 0 else 2.0.pow(s-1).toInt() }
        }
        part2(30, 7258152) { it.copyScoring() }
    }

    private fun List<Int>.copyScoring() : Int {
        val scoreSheet = mutableMapOf<Int, Int>()
        reversed().forEachIndexed { i, score ->
            scoreSheet[i] = (i downTo i-score).sumOf { scoreSheet[it] ?: 0 } + 1
        }
        return scoreSheet.values.sum()
    }
    private fun String.toScratchCard() = chunked(3).map(String::trim).toSet()

    override val example = """
        Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
        Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
        Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
        Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
        Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
    """.trimIndent()
}