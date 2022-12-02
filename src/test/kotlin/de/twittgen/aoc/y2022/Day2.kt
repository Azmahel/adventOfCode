package de.twittgen.aoc.y2022

import de.twittgen.aoc.y2019.shared.util.FileUtil
import de.twittgen.aoc.y2022.Day2.Result.*
import de.twittgen.aoc.y2022.Day2.Symbol.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day2 {
    val input by lazy { parseInput(FileUtil.readInput("2022/day2")) }
    val example = """
        A Y
        B X
        C Z
    """.trimIndent()

    private fun parseInput(input: String): List<Pair<Char, Char>> =
        input.lines().map {
            val chars = it.split(" ")
            chars[0].first() to chars[1].first()
        }

    @Test
    fun part1Example() {
        val result = parseInput(example)
            .map { exampleKey[it.first]!! to exampleKey[it.second]!! }
            .sumOf { playRound(it) }
        println(result)
        assertEquals(15, result)
    }

    @Test
    fun part1() {
        val result = input
            .map { exampleKey[it.first]!! to exampleKey[it.second]!! }
            .sumOf { playRound(it) }
        println(result)
        assertEquals(13924, result)
    }

    @Test
    fun part2Example() {
        val result = parseInput(example)
            .map { symbolKey[it.first]!! to resultKey[it.second]!! }
            .map { it.first to getSymbolForResult(it) }
            .sumOf { playRound(it) }

        println(result)
        assertEquals(12, result)
    }

    @Test
    fun part2() {
        val result = input
            .map { symbolKey[it.first]!! to resultKey[it.second]!! }
            .map { it.first to getSymbolForResult(it) }
            .sumOf { playRound(it) }

        println(result)
        assertEquals(13448, result)
    }

    private fun getSymbolForResult(wanted: Pair<Symbol, Result>):  Symbol {
        val (enemyMove, wantedResult) = wanted
        return moves[wantedResult]!!.find{ it.first == enemyMove }!!.second
    }

    private enum class Symbol {
        PAPER, ROCK, SCISSORS,
    }

    private enum class Result {
        WIN, LOOSE, DRAW
    }

    private val rules = mapOf(
        (ROCK to ROCK) to DRAW,
        (ROCK to SCISSORS) to LOOSE,
        (ROCK to PAPER) to WIN,
        (SCISSORS to SCISSORS) to DRAW,
        (SCISSORS to ROCK) to WIN,
        (SCISSORS to PAPER) to LOOSE,
        (PAPER to PAPER) to DRAW,
        (PAPER to SCISSORS) to WIN,
        (PAPER to ROCK) to LOOSE
    )

    private val moves = rules.entries.groupBy { it.value }
        .mapValues { it.value.map { it.key } }

    private val exampleKey = mapOf(
        'A' to ROCK,
        'B' to PAPER,
        'C' to SCISSORS,
        'X' to ROCK,
        'Y' to PAPER,
        'Z' to SCISSORS,
    )
    private val symbolKey = mapOf(
        'A' to ROCK,
        'B' to PAPER,
        'C' to SCISSORS,
    )
    private val resultKey = mapOf(
        'X' to LOOSE,
        'Y' to DRAW,
        'Z' to WIN
    )

    private val gameScores = mapOf(
        WIN to 6, DRAW to 3, LOOSE to 0,
    )

    private val symbolScores = mapOf(
        ROCK to 1, PAPER to 2, SCISSORS to 3
    )

    private fun playRound(chosen: Pair<Symbol, Symbol>) =
       gameScores[rules[chosen]]!! + symbolScores[chosen.second]!!
}