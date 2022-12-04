package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day

typealias Rucksack = Pair<Set<Char>,Set<Char>>

class Day3 : Day<Int, Int, List<Rucksack>>() {
    override val  example = """
        vJrwpWtwJgWrhcsFMMfFFhFp
        jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
        PmmdzqPrVvPwwTWBwg
        wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
        ttgJtRGJQctTZtZT
        CrZsJsPPZsGzwwsLwLmpwMDw
    """.trimIndent()

    override fun String.parse() = lines().map { it.toRucksack() }

    init {
        part1(157, 8085) {
            map { (a, b) -> a.intersect(b).single() }.sumOf { priorities[it]!! }
        }
        part2(70, 2515) {
            findBadges().sumOf { priorities[it]!! }
        }
    }

    private val alphabet = ('a'..'z')+('A'..'Z')
    private val priorities: Map<Char, Int> = (alphabet).mapIndexed { i, c -> c to i+1 }.toMap()

    private fun String.toRucksack() = chunked(length/2).let { it[0].toSet() to it[1].toSet() }

    private fun List<Rucksack>.findBadges() =
        chunked(3).map { group ->
            group
                .map { it.first + it.second }
                .fold(alphabet.toSet()) { a, b -> a.intersect(b) }
                .single()
        }
}


