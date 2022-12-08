package de.twittgen.aoc.y2020

import de.twittgen.aoc.util.FileUtil
import de.twittgen.aoc.util.second
import de.twittgen.aoc.util.times
import org.junit.jupiter.api.Test

typealias RuleSet = Map<String, List<Pair<Int, String>>>
class day7 {
    val input = FileUtil.readInput("2020/day7")
    val example= """
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

    fun parseInput(s: String): RuleSet {
        return s
            .replace("bags", "")
            .replace("bag", "")
            .replace(".","")
            .lines()
            .associate {
                with(it.split(" contain ")) {
                    first().trim() to parseContent(second())
                }
        }
    }

    private fun parseContent(s : String): List<Pair<Int, String>> {
        return s
            .split(", ")
            .mapNotNull {
                if(it == "no other "){
                    null
                } else {
                    val (number, color) = Regex("([0-9]*) (.*)")
                        .matchEntire(it)!!
                        .destructured
                    number.toInt() to color.trim()
                }

        }
    }

    @Test
    fun example() {
        val rules = parseInput(example)
        val start = listOf("shiny gold")
        val outerBags = rules.getPossibleContainingBags(start)
        assert(
            outerBags.size ==4
        )
    }

    @Test
    fun part1() {
        val rules = parseInput(input)
        val start = listOf("shiny gold")
        val outerBags = rules.getPossibleContainingBags(start)
        println(
            outerBags.size
        )
    }

    @Test
    fun part2() {
        val rules = parseInput(input)
        val start = listOf("shiny gold")
        val innerBags = rules.getInnerBags(start)
        println(innerBags.size)
    }

    private tailrec fun RuleSet.getPossibleContainingBags(bags: List<String>, acc: List<String> = emptyList()): List<String> {
        val outerbags = entries.filter { (_, value) ->
            value.map { it.second }.any { it in bags}
        }.map { it.key }
        return if(outerbags.isEmpty())
            acc
        else
            getPossibleContainingBags(outerbags, (acc + outerbags).distinct())
    }

}

private tailrec fun RuleSet.getInnerBags(start: List<String>, acc: List<String> = emptyList()): List<String> {
    val innerbags = start
        .flatMap {
            get(it)!!
        }.flatMap { (count, color) ->
            listOf(color) * count
        }
    return if (innerbags.isEmpty())
        acc
    else
        getInnerBags(innerbags, acc+ innerbags)
}


