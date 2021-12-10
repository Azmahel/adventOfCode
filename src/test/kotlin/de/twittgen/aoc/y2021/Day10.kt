package de.twittgen.aoc.y2021

import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day10 {
    val input by lazy { FileUtil.readInput("2021/day10").parse() }
    val example = """[({(<(())[]>[[{[]{<()<>>
[(()[<>])]({[<{<<[]>>(
{([(<{}[<>[]}>{[]{[(<()>
(((({<>}<{<{<>}{[]{[]{}
[[<[([]))<([[{}[[()]]]
[{[{({}]{}}([{[{{{}}([]
{<[[]]>}<{[{[{[]{()[[[]
[<(<(<(<{}))><([]([]()
<{([([[(<>()){}]>(<<{{
<{([{{}}[<[[[<>{}]]]>[]]""".parse()

    sealed class ParseResult()
    class Error(val c: Char) : ParseResult()
    class Success(val missing: List<Char>) : ParseResult()
    private fun String.parse() = lines().map { it.toList() }
    private val openers = mapOf(
        ']' to '[',
        ')' to '(',
        '>' to '<',
        '}' to '{'
        )
    private val closing = listOf(']', '>', ')', '}')
    private val opening = listOf('[', '<', '(', '{')

    private fun Char.isOpening() = this in opening
    private fun Char.isClosing() = this in closing

    fun List<Char>.parse() : ParseResult {
        val opening = mutableListOf<Char>()
        forEach {
            if(it.isOpening()) opening.add(it)
            if(it.isClosing()) {
                if(opening.last() != openers[it]) return Error(it) else opening.removeLast()
            }
        }
        return Success(opening.map { toClose -> openers.filterValues { it == toClose }.keys.first() }.reversed())
    }

    private fun Char.score() = when(this) {
        ']' -> 57
        ')' -> 3
        '>' -> 25137
        '}' -> 1197
        else -> 0
    }

    private fun List<Long>.middle() = get((size-1) /2)

    private fun List<List<Char>>.score() = map { it.scoreMissing() }.sorted().middle()

    private fun  List<Char>.scoreMissing() : Long =
        fold(0L) { i , it ->
            i*5 + when(it) {
                ']' -> 2
                ')' -> 1
                '>' -> 4
                '}' -> 3
                else -> 0
            }
        }



    @Test
    fun example() {
        val result = example.map { it.parse() }.filterIsInstance<Error>().sumOf { it.c.score() }
        assertEquals(26397, result)
    }

    @Test
    fun example2() {
        val result = example.map { it.parse() }.filterIsInstance<Success>().map { it.missing }.score()
        assertEquals(288957, result)
    }

    @Test
    fun part1() {
        val result : Int = input.map { it.parse() }.filterIsInstance<Error>().sumOf { it.c.score() }
        println(result)
    }

    @Test
    fun part2() {
        val result = input.map { it.parse() }.filterIsInstance<Success>().map { it.missing }.score()
        println(result)
    }
}



