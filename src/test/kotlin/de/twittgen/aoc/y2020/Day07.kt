package de.twittgen.aoc.y2020

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.groups
import de.twittgen.aoc.util.times

class Day07 : Day<RuleSet>() {
    private val bagMatcher = Regex("(.*) bags contain (.*)")
    override fun String.parse() = lines().associate {
        bagMatcher.groups(it)!!.let {(a,b) ->  a.trim() to b.parseContent() }
    }

    private val contentMatcher = Regex("([0-9]*) (.*)bag(s*)\\.*")
    private fun String.parseContent() = split(", ").mapNotNull {
        contentMatcher.matchEntire(it)?.destructured?.let { (number, color) -> number.toInt() to color.trim() }
    }

    private val shinyGold = listOf("shiny gold")
    init {
        part1(4, 101) { shinyGold.getPossibleContainingBags(it).size }
        part2(32, 108636) { shinyGold.getInnerBags(it).size }
    }

    private tailrec fun List<String>.getInnerBags(rules: RuleSet, acc: List<String> = emptyList()): List<String> {
        val inside = flatMap { rules[it]!! }.flatMap { (count, color) -> listOf(color) * count }
        return if (inside.isEmpty()) acc else inside.getInnerBags(rules, acc + inside)
    }

    private tailrec fun List<String>.getPossibleContainingBags(rules: RuleSet, acc: List<String> = emptyList()): List<String> {
        val outside = rules.entries.filter { (_, value) -> value.map { it.second }.any { it in this } }.map { it.key }
        return if(outside.isEmpty()) acc else outside.getPossibleContainingBags(rules, (acc + outside).distinct())
    }

    override val example= """
        light red bags contain 1 bright white bag, 2 muted yellow bags.
        dark orange bags contain 3 bright white bags, 4 muted yellow bags.
        bright white bags contain 1 shiny gold bag.
        muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
        shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
        dark olive bags contain 3 faded blue bags, 4 dotted black bags.
        vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
        faded blue bags contain no other bags.
        dotted black bags contain no other bags.
    """.trimIndent()
}

private typealias RuleSet = Map<String, List<Pair<Int, String>>>


