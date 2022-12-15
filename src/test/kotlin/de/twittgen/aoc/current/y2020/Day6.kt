package de.twittgen.aoc.current.y2020

import de.twittgen.aoc.Day

class Day6 : Day<List<List<String>>>(){
    override fun String.parse() = split("\n\n").map { it.lines() }

    init {
        part1(11, 6297) { it.sumOf { group -> questions.filter { q -> group.any { p -> p.contains(q) } }.size } }
        part2(6, 3158) { it.sumOf { group -> questions.filter { q -> group.all { p -> p.contains(q) } }.size } }
    }

    private val questions = ('a'..'z').toList()

    override val example = """
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
}