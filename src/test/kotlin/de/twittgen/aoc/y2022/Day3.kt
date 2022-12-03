package de.twittgen.aoc.y2022

import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

typealias Rucksack = Pair<Set<Char>,Set<Char>>

class Day3 {
    val input by lazy { parseInput(FileUtil.readInput("2022/day3")) }
    val example = """
        vJrwpWtwJgWrhcsFMMfFFhFp
        jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
        PmmdzqPrVvPwwTWBwg
        wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
        ttgJtRGJQctTZtZT
        CrZsJsPPZsGzwwsLwLmpwMDw
    """.trimIndent()

    private val alphabet = ('a'..'z')+('A'..'Z')
    private val priorities: Map<Char, Int> = (alphabet).mapIndexed { i, c -> c to i+1 }.toMap()

    private fun parseInput(input: String) = input.lines().map { it.toRucksack() }

    @Test
    fun part1Example() {
        val result = parseInput(example)
            .map { (a, b) -> a.intersect(b).single() }
            .sumOf { priorities[it]!! }
        println(result)
        assertEquals(157, result)
    }

    @Test
    fun part1() {
        val result = input
            .map { (a, b) -> a.intersect(b).single() }
            .sumOf { priorities[it]!! }
        println(result)
        assertEquals(8085, result)
    }

    @Test
    fun part2Example() {
        val result = parseInput(example)
            .findBadges()
            .sumOf { priorities[it]!! }
        println(result)
        assertEquals(70, result)
    }

    @Test
    fun part2() {
        val result = input
            .findBadges()
            .sumOf { priorities[it]!! }
        println(result)
        assertEquals(2515, result)
    }


    private fun String.toRucksack() = chunked(length/2)
        .let { it[0].toSet() to it[1].toSet() }

    private fun List<Rucksack>.findBadges() = chunked(3)
        .map { group ->
            group
                .map { it.first + it.second }
                .fold(alphabet.toSet()) { a, b -> a.intersect(b) }
                .single()
        }
}


