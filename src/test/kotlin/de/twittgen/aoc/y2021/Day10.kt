package de.twittgen.aoc.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.middle
import java.util.*

class Day10: Day<List<Line>>() {
    override fun String.parse() = lines().map { it.toList() }

    init {
        part1(26397, 296535) { lines ->
           lines.map { it.parse() }.filterIsInstance<Error>().sumOf { it.char.score() }
        }
        part2(288957, 4245130838) { lines ->
            lines.map { it.parse() }.filterIsInstance<Success>().map(Success::missingChars).score()
        }
    }

    fun Line.parse(): ParseResult {
        val unclosed = Stack<Char>()
        forEach { with(it) { when {
            isOpening() -> unclosed.add(this)
            isClosing() -> if (unclosed.peek() != openers[this]) return Error(this) else unclosed.pop()
            else -> return Error(this)
        } } }
        return Success(unclosed.map { toClose -> closers[toClose]!! }.reversed())
    }

    private fun List<Line>.score() = map { it.scoreLine() }.sorted().middle()
    private fun Char.score() = scores[this]!!
    private fun Char.isOpening() = this in listOf('[', '<', '(', '{')
    private fun Char.isClosing() = this in listOf(']', '>', ')', '}')
    private fun Line.scoreLine() = fold(0L) { i, it -> i * 5 + missingScores[it]!! }

    private val openers = mapOf(']' to '[', ')' to '(', '>' to '<', '}' to '{')
    private val closers = openers.entries.associate { (k, v) -> v to k }
    private val scores = mapOf(']' to 57, ')' to 3, '>' to 25137, '}' to 1197)
    private val missingScores = mapOf(']' to 2, ')' to 1, '>' to 4, '}' to 3)

    sealed class ParseResult
    class Error(val char: Char) : ParseResult()
    class Success(val missingChars: List<Char>) : ParseResult()

    override val example = """
        [({(<(())[]>[[{[]{<()<>>
        [(()[<>])]({[<{<<[]>>(
        {([(<{}[<>[]}>{[]{[(<()>
        (((({<>}<{<{<>}{[]{[]{}
        [[<[([]))<([[{}[[()]]]
        [{[{({}]{}}([{[{{{}}([]
        {<[[]]>}<{[{[{[]{()[[[]
        [<(<(<(<{}))><([]([]()
        <{([([[(<>()){}]>(<<{{
        <{([{{}}[<[[[<>{}]]]>[]]
    """.trimIndent()
}
private typealias Line = List<Char>
