package de.twittgen.aoc.y2024

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.*

class Day05 : Day<Update>() {
    override fun String.parse() = split(emptyLine).let { (rules, pages) -> rules.parseRules() to pages.parsePages() }
    private fun String.parseRules() = mapLines { it.split("|").let { (a,b) -> a.toInt() to b.toInt() } }.toSet()
    private fun String.parsePages() = mapLines { it.split(",").map(String::toInt) }
    init {
        part1(143, 6260) { (rules, lines) ->
            lines.filter { rules.fit(it.toIndexMap()) }.sumOf { l -> l[l.lastIndex/2] }
        }
        part2(123, 5346) { (rules, lines) ->
            lines.filter { !rules.fit(it.toIndexMap()) }.map { it.sortByRules(rules) }.sumOf { l -> l[l.lastIndex/2] }
        }
    }

    private fun Pages.toIndexMap() = mapIndexed { i, it -> it to i }.toMap()
    private fun Set<Rule>.fit(map : Map<Int, Int>) = all { (a,b) -> a !in map || b!in map || map[a]!! < map[b]!! }
    private fun List<Int>.sortByRules(rules: Set<Rule>) = sortedWith { a,b -> when {
        a to b in rules -> -1; b to a in rules -> 1; else -> 0
    }}

    override val example = """
        47|53
        97|13
        97|61
        97|47
        75|29
        61|13
        75|53
        29|13
        97|29
        53|29
        61|53
        97|53
        61|29
        47|13
        75|47
        97|75
        47|61
        75|61
        47|29
        75|13
        53|13

        75,47,61,53,29
        97,61,53,29,13
        75,29,13
        75,97,47,61,53
        61,13,29
        97,13,75,29,47
    """.trimIndent()
}
private typealias Rule = Pair<Int,Int>
private typealias Pages = List<Int>
private  typealias Update = Pair< Set<Rule>, List<Pages>>