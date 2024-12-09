package de.twittgen.aoc.y2023

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.emptyLine
import de.twittgen.aoc.util.second

class Day19: Day<System>()
{
    override fun String.parse() = split(emptyLine).let { (a, b) -> a.toWorkflows() to b.toParts() }

    init {
        part1(19114, 374873) { (w, t) ->
            t.filter { it.process(w) == "A" }.sumOf { it["x"]!! + it["m"]!! + it["a"]!! + it["s"]!! }
        }
    }

    private fun Thing.process(w: Workflows): String {
        var current = "in"
        while (current in w) { current = w[current]!!.map { it.toRule() }.first {  it.requirement(this)}.target }
        return current
    }

    private val workflowRegex = Regex("(.+)\\{(.+)}")
    private fun String.toWorkflows() = lines().associate {
        workflowRegex.matchEntire(it)!!.groupValues.drop(1).let {(a,b) -> a to  b.split(",") }
    }
    private fun String.toParts() = lines().map {
        it.drop(1).dropLast(1).split(",").associate { it.split("=").let { (a, b) -> a to b.toInt() } }
    }
    private fun String.toRule() =
        split(":").let {
            if (it.size==1) Rule(it.first(), noRequirement) else Rule(it.second(), it.first().toRequirement())
        }
    private fun String.toRequirement(): (Thing) -> Boolean = if (contains(">"))  {
        split(">").let { (a,b)  ->  { it[a]!! > b.toInt() } }
    } else {
        split("<").let { (a,b)  -> { it[a]!! < b.toInt() } }
    }

    override val example = """
        px{a<2006:qkq,m>2090:A,rfg}
        pv{a>1716:R,A}
        lnx{m>1548:A,A}
        rfg{s<537:gd,x>2440:R,A}
        qs{s>3448:A,lnx}
        qkq{x<1416:A,crn}
        crn{x>2662:A,R}
        in{s<1351:px,qqz}
        qqz{s>2770:qs,m<1801:hdj,R}
        gd{a>3333:R,R}
        hdj{m>838:A,pv}

        {x=787,m=2655,a=1222,s=2876}
        {x=1679,m=44,a=2067,s=496}
        {x=2036,m=264,a=79,s=2244}
        {x=2461,m=1339,a=466,s=291}
        {x=2127,m=1623,a=2188,s=1013}
    """.trimIndent()

    private val noRequirement: (Thing)-> Boolean = { true }
    private data class Rule(val target: String, val requirement: (Thing)-> Boolean)
}

private typealias System = Pair<Workflows, List<Thing>>
private typealias Thing = Map<String, Int>
private typealias Workflow = List<String>
private typealias Workflows = Map<String, Workflow>