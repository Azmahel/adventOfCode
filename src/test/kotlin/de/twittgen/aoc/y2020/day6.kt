package de.twittgen.aoc.y2020

import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Test

class day6 {
    val input = FileUtil.readInput("2020/day6")
    val example = """
        abc

        a
        b
        c

        ab
        ac

        a
        a
        a
        a

        b
    """.trimIndent()

    fun readInput(s: String): List<List<String>> = s.replace("\r","").split("\n\n").map { it.lines() }

    @Test
    fun example() {
        val groups = readInput(example)
        val result = groups.map { it.getAny().size }.sum()
        assert( result == 11)
    }

    @Test
    fun part1() {
        val groups = readInput(input)
        val result = groups.map { it.getAny().size }.sum()
        println(result)
    }

    @Test
    fun part2() {
        val groups = readInput(input)
        val result = groups.map { it.getAll().size }.sum()
        println(result)
    }

    fun List<String>.getAll(questions: List<Char> = ('a'..'z').toList()): List<Char> {
        return  questions.filter { q->  all { it.contains(q) } }
    }
    fun List<String>.getAny(questions: List<Char> = ('a'..'z').toList()): List<Char> {
       return  questions.filter { q->  any { it.contains(q) } }
    }


}