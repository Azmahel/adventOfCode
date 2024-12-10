package de.twittgen.aoc.y2023

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.emptyLine
import de.twittgen.aoc.util.second
import de.twittgen.aoc.util.takeUntil
import de.twittgen.aoc.y2023.Day19.Rule

class Day19: Day<System>()
{
    override fun String.parse() = split(emptyLine).let { (a, b) -> a.toWorkflows() to b.toParts() }

    init {
        part1(19114, 374873) { (w, t) ->
            t.filter { it.process(w) == "A" }.sumOf { it["x"]!! + it["m"]!! + it["a"]!! + it["s"]!! }
        }
        part2(167409079868000, 122112157518711) { (w,_) ->
            w["in"]!!.collapse(w).sumOf { it.toRanges().possibleMatches() }
        }
    }

    private fun Thing.process(w: Workflows): String {
        var current = "in"
        while (current in w) { current = w[current]!!.map { it }.first {  it.match(this) }.target }
        return current
    }

    private fun Workflow.collapse(map :Workflows) : List<List<ClauseGuard>> = takeUntil{ it.match is NoReq }
        .fold(emptyList<ClauseGuard>() to emptyMap<Rule, List<ClauseGuard>>()) { (guards, sequences), rule ->
            if (rule.match is ClauseGuard) {
                (guards + rule.match.invert()) to (sequences + (rule to (guards + rule.match)))
            } else {
                guards to (sequences + (rule to guards))
            }
        }.second.flatMap { (rule, guards) -> when(rule.target) {
            "A" -> listOf(guards); "R" -> emptyList(); else -> map[rule.target]!!.collapse(map).map { guards + it }
        }}
    private fun ClauseGuard.invert() = when(this) {is Gt -> Lt(a, b + 1); is Lt -> Gt(a, b - 1) }
    private fun List<ClauseGuard>.toRanges()= groupBy { it.a }
        .mapValues { (_, guards) ->
            (guards + Gt("", 0) + Lt("", 4001)).sortedBy { it.b }
                .windowed(2) { (g1, g2) -> if(g1 is Gt && g2 is Lt) (g1.b +1)..<g2.b else null }
                    .filterNotNull()
        }

    private fun Map<String, List<IntRange>>.possibleMatches() =
        listOf("x", "m", "a", "s").map { s -> rangeOf(s) }.fold(1L) { a, c -> a * c }

    private fun Map<String, List<IntRange>>.rangeOf(s: String) =
        getOrDefault(s, listOf(1..4000)).fold(1L) { i, v -> i * (v.last - v.first + 1) }

    private val workflowRegex = Regex("(.+)\\{(.+)}")
    private fun String.toWorkflows() = lines().associate {
        workflowRegex.matchEntire(it)!!.groupValues.drop(1).let { (a,b) ->
            a to b.split(",").map { it.toRule()  }
        }
    }
    private fun String.toParts() = lines().map {
        it.drop(1).dropLast(1).split(",")
            .associate { it.split("=").let { (a, b) -> a to b.toInt() } }
    }
    private fun String.toRule() = split(":").let {
        if (it.size == 1) Rule(it.first(), NoReq) else Rule(it.second(), it.first().toGuard())
    }

    private val guardRegex = Regex("(.*)([<>])(.*)")
    private fun String.toGuard() = guardRegex.matchEntire(this)!!.groupValues.let { (_,a,o,b) -> when(o) {
        ">" -> Gt(a, b.toInt()); "<" -> Lt(a, b.toInt()); else -> throw IllegalStateException()
    }}

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

    data class Rule(val target: String, val match: Guard)
    sealed class Guard { abstract operator fun invoke(t: Thing): Boolean }
    private data object NoReq: Guard() { override fun invoke(t: Thing) = true }
    private sealed class ClauseGuard(val a: String, val b: Int): Guard()
    private class Gt(a:String,  b:Int): ClauseGuard(a, b) { override fun invoke(t: Thing) = t[a]!! > b }
    private class Lt(a:String, b:Int): ClauseGuard(a, b) { override fun invoke(t: Thing) = t[a]!! < b }
}

private typealias System = Pair<Workflows, List<Thing>>
private typealias Thing = Map<String, Int>
private typealias Workflow = List<Rule>
private typealias Workflows = Map<String, Workflow>