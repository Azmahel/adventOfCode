package de.twittgen.aoc.y2024

import de.twittgen.aoc.Day
import de.twittgen.aoc.Day.TestMarker.SLOW
import de.twittgen.aoc.util.mapLines
import de.twittgen.aoc.util.permutationsOf
import de.twittgen.aoc.util.second

class Day07 : Day<List<Operation>>() {
    override fun String.parse() = mapLines { it.split(": ").let { l ->
        l.first().toLong() to l.second().split(" ").map(String::toLong)
    }}

    init {
        part1(3749,663613490587) {
            it.filter { (t, o) -> o.testForallPossibleOperators(t) }.sumOf(Operation::first)
        }
        part2(11387,110365987435001, SLOW) { //tired improving runtime with memoization, but made it slower
            it.filter { (t, o) -> o.testForallPossibleOperators(t, allOperators) }.sumOf(Operation::first)
        }
    }

    private val operators = listOf(MULT, ADD)
    private val allOperators = listOf(MULT, ADD, CONCAT)
    private fun List<Long>.testForallPossibleOperators(expected: Long, op: List<Operator> = operators) =
        op.permutationsOf(lastIndex).any{ p -> p.apply(this, expected) == expected  }


    private fun List<Operator>.apply(numbers: List<Long>, expected: Long) =
        numbers.reduceIndexed { i , x, y -> if(x > expected) { return@apply -1L }else { get(i-1)(x,y) }}


    override val example = """
        190: 10 19
        3267: 81 40 27
        83: 17 5
        156: 15 6
        7290: 6 8 6 15
        161011: 16 10 13
        192: 17 8 14
        21037: 9 7 18 13
        292: 11 6 16 20
    """.trimIndent()

    private sealed class Operator { abstract operator fun invoke(x: Long, y:Long) : Long }
    private data object MULT: Operator() { override fun invoke(x: Long, y: Long) = x * y }
    private data object ADD: Operator() { override fun invoke(x: Long, y: Long) = x + y }
    private data object CONCAT: Operator() { override fun invoke(x: Long, y: Long) = "$x$y".toLong() }
}
private typealias Operation = Pair<Long, List<Long>>