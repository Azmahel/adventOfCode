package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.y2019.shared.util.FileUtil
import de.twittgen.aoc.y2022.Day2.Result.*
import de.twittgen.aoc.y2022.Day2.Symbol.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
typealias Move = Pair<Char,Char>
class Day2: Day<Int, Int, List<Move>>() {
    override val example = """
        A Y
        B X
        C Z
    """.trimIndent()

    override fun String.parse() = lines().map { it.split(" ").let { it[0].single() to it[1].single() } }

    init {
        part1(15, 13924) {
            map { exampleKey[it.first]!! to exampleKey[it.second]!! }.sumOf { playRound(it) }
        }
        part2(12, 13448) {
            map {
                (symbolKey[it.first]!! to resultKey[it.second]!!).let { it.first to getSymbolForResult(it) }
            }.sumOf { playRound(it) }
        }
    }

    private fun getSymbolForResult(wanted: Pair<Symbol, Result>):  Symbol {
        val (enemyMove, wantedResult) = wanted
        return moves[wantedResult]!!.find{ it.first == enemyMove }!!.second
    }

    private enum class Symbol { PAPER, ROCK, SCISSORS, }
    private enum class Result { WIN, LOOSE, DRAW }

    private val rules = mapOf(
        (ROCK to ROCK) to DRAW, (ROCK to SCISSORS) to LOOSE, (ROCK to PAPER) to WIN,
        (SCISSORS to SCISSORS) to DRAW, (SCISSORS to ROCK) to WIN, (SCISSORS to PAPER) to LOOSE,
        (PAPER to PAPER) to DRAW, (PAPER to SCISSORS) to WIN, (PAPER to ROCK) to LOOSE
    )

    private val moves = rules.entries.groupBy { it.value }.mapValues { it.value.map { it.key } }
    private val exampleKey = mapOf('A' to ROCK, 'B' to PAPER, 'C' to SCISSORS, 'X' to ROCK, 'Y' to PAPER, 'Z' to SCISSORS,)
    private val symbolKey = mapOf('A' to ROCK, 'B' to PAPER, 'C' to SCISSORS,)
    private val resultKey = mapOf('X' to LOOSE, 'Y' to DRAW, 'Z' to WIN)
    private val gameScores = mapOf(WIN to 6, DRAW to 3, LOOSE to 0,)
    private val symbolScores = mapOf(ROCK to 1, PAPER to 2, SCISSORS to 3)

    private fun playRound(chosen: Pair<Symbol, Symbol>) = gameScores[rules[chosen]]!! + symbolScores[chosen.second]!!
}