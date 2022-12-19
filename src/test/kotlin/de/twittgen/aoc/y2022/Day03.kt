package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.alphabet


class Day03: Day<List<Rucksack>>() {
    override fun String.parse() = lines().map { it.toRucksack() }

    init {
        part1(157, 8085) {
            it.map { (a, b) -> a.intersect(b).single() }.sumOf { p -> priorities[p]!! }
        }
        part2(70, 2515) { it.findBadges().sumOf { b-> priorities[b]!! } }
    }

    private val priorities: Map<Char, Int> = (alphabet).mapIndexed { i, c -> c to i+1 }.toMap()

    private fun String.toRucksack() = chunked(length/2).let { it[0].toSet() to it[1].toSet() }

    private fun List<Rucksack>.findBadges() = chunked(3).map { group ->
        group.map { it.allContents }
            .fold(alphabet.toSet()) { a, b -> a.intersect(b) }
            .single()
    }

    override val  example = """
        vJrwpWtwJgWrhcsFMMfFFhFp
        jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
        PmmdzqPrVvPwwTWBwg
        wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
        ttgJtRGJQctTZtZT
        CrZsJsPPZsGzwwsLwLmpwMDw
    """.trimIndent()
}
 private typealias Rucksack = Pair<Set<Char>,Set<Char>>
private val Rucksack.allContents  get() = first +second

