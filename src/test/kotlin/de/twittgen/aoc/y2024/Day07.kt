package de.twittgen.aoc.y2024

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.mapLines
import de.twittgen.aoc.util.second

class Day07 : Day<List<Operation>>() {
    override fun String.parse() = mapLines { it.split(": ").let { l ->
        l.first().toLong() to l.second().split(" ").map(String::toLong)
    }}

    init {
        part1(3749,663613490587) {
            it.filter { op -> operators.testOperators(op) }.sumOf(Operation::first)
        }
        part2(11387,110365987435001) {
            it.filter { op -> allOperators.testOperators(op) }.sumOf(Operation::first)
        }
    }

    private val operators = listOf(MULT, ADD)
    private val allOperators = listOf(MULT, ADD, CONCAT)
    private fun List<Operator>.testOperators(op: Operation) = testOperators(op.first, op.second.drop(1), op.second.first())
    private fun List<Operator>.testOperators(expected: Long, numbers: List<Long>, current: Long): Boolean {
        if (current > expected) return false
        if (numbers.isEmpty()) return expected == current
        return map { it(current, numbers.first()) }
            .map { testOperators(expected, numbers.drop(1), it) }
            .any { it }
    }

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