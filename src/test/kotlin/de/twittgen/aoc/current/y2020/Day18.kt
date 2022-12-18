package de.twittgen.aoc.current.y2020

import de.twittgen.aoc.current.Day

class Day18 : Day<List<String>>() {
    override fun String.parse() = lines()

    init {
        part1(26406, 36382392389406) { it.sumOf { task -> compute(task) } }
        part2(694122, 381107029777968) { it.sumOf { task -> computeV2(task) }}
    }

    private fun compute(s: String): Long {
       val current = s.computeParentheses(::compute)
        return current.split(" ").fold({x: Long -> x}) { acc, it -> when(it) {
            "+" -> {x -> acc(0) + x }
            "*" -> {x -> acc(0) * x }
            else -> {_ -> acc(it.toLong())}
        } }(0)
    }

    private fun String.computeParentheses(c: String.() -> Long): String {
        var current = this
        while (current.contains("(")) {
            val subExpr = current.takeLastWhile { it != '(' }.takeWhile { it != ')' }
            current = current.replace("($subExpr)", c(subExpr).toString())
        }
        return current
    }

    private fun computeV2(s: String): Long {
        var current = s.computeParentheses(::computeV2)
        while (current.contains(" + ")) {
            val index = current.indexOf("+")
            val x = current.substring(0, index).dropLast(1).takeLastWhile { it != ' ' }.toLong()
            val y = current.substring(index).drop(2).takeWhile { it != ' ' }.toLong()
            val result = x +y
            current = current.replaceFirst("$x + $y", result.toString())
        }
        return current.split(" ").fold({x: Long -> x}) { acc, it -> when(it) {
            "*" -> {x -> acc(0) * x }
            else -> {_ -> acc(it.toLong())}
        } }(0)
    }

    override val example = """
        1 + 2 * 3 + 4 * 5 + 6
        2 * 3 + (4 * 5)
        5 + (8 * 3 + 9 + 3 * 4 * 3)
        5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))
        ((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2
    """.trimIndent()
}