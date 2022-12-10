package de.twittgen.aoc.old.y2020

import de.twittgen.aoc.util.FileUtil
import org.junit.jupiter.api.Test

class day18 {
    val input = FileUtil.readInput("2020/day18")
    val example = """
        1 + 2 * 3 + 4 * 5 + 6
        2 * 3 + (4 * 5)
        5 + (8 * 3 + 9 + 3 * 4 * 3)
        5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))
        ((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2
    """.trimIndent()


    fun parseInput(s: String) = s.lines()


    @Test
    fun example() {
        val operations = parseInput(example)
        val result = operations.map { it.compute() }
        val x =0
    }

    @Test
    fun part1() {
        val operations = parseInput(input)
        val result = operations.map { it.compute() }
        println(result.sum())
    }

    @Test
    fun part2() {
        val operations = parseInput(input)
        val result = operations.map { it.computeV2() }
        println(result.sum())
    }

    fun String.compute() : Long {
       var current = this
        while(current.contains("(")) {
            val subExpr = current
                .takeLastWhile { it != '(' }
                .takeWhile { it != ')' }
            val result = subExpr.compute()
            current = current.replace("($subExpr)", result.toString())
        }
        return current.split(" ").fold({x: Long? -> x}) { acc, it ->
            when(it) {
                "+" -> {x -> acc(null)!! + x!! }
                "*" -> {x -> acc(null)!! * x!! }
                else -> {_ -> acc(it.toLong())}
            }
        }(null)!!
    }

    fun String.computeV2() : Long {
        var current = this
        while(current.contains("(")) {
            val subExpr = current
                .takeLastWhile { it != '(' }
                .takeWhile { it != ')' }
            val result = subExpr.computeV2()
            current = current.replace("($subExpr)", result.toString())
        }
        while (current.contains(" + ")) {
            val index = current.indexOf("+")
            val x = current.substring(0, index)
                .dropLast(1)
                .takeLastWhile { it != ' ' }
                .toLong()
            val y = current.substring(index)
                .drop(2)
                .takeWhile { it != ' ' }
                .toLong()
            val result = x +y
            current = current.replaceFirst("$x + $y", result.toString())
        }
        return current.split(" ").fold({x: Long? -> x}) { acc, it ->
            when(it) {
                "*" -> {x -> acc(null)!! * x!! }
                else -> {_ -> acc(it.toLong())}
            }
        }(null)!!
    }
}