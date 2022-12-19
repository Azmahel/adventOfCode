package de.twittgen.aoc.current.y2020

import de.twittgen.aoc.current.Day
import de.twittgen.aoc.util.isNumber


class Day19 : Day<Pair<Grammar, List<String>>>() {
    override fun String.parse() = split("\n\n").let { (rawRules, inputs) ->
        val rules = rawRules.lines().associate { it.replace("\"", "").parseRule() }
        rules to inputs.lines()
    }

    private fun String.parseRule() = split(":").let { (a, b) ->
        a to b.split("|").map { it.split(" ").filter { r -> r.isNotBlank() } }
    }

    private val replacements = """
        8: 42 | 42 8
        11: 42 31 | 42 11 31
    """.trimIndent().lines()

    init {
        part1(3, 120) {  (rules, inputs ) ->
            val regex = resolveRules(rules)["0"]!!.toRegex()
            inputs.filter { l -> regex.matches(l) }.size
        }
        part2(12, 350) { (rules, input) ->
           val newRules = rules + replacements.map { it.parseRule() }
            val resolved = resolveRules(newRules)
            val eight = resolved["8"]!!.replace("8","+")
            //assuming no deeper loop than 20
            val eleven = (1..20).joinToString("|", "(?:", ")")
            { "(?:${resolved["42"]}){$it}(?:${resolved["31"]}){$it}" }
            val regex = resolved["0"]!!.replace("8",eight).replace("11",eleven ).toRegex()
            input.filter { regex.matches(it) }.size
        }
    }

    private fun resolveRules(rules: Grammar): MutableMap<String, String> {
        val resolvedRules = rules.filter { isResolved(it.toPair()) }.mapValues { (_,v) -> v.toRegex() }.toMutableMap()
        var current = rules.filterNot { (k,_) -> k in resolvedRules }
        while(current.isNotEmpty()) {
            val next = current.mapValues { (_,v) -> v.map { it.map {
                if(it in resolvedRules.keys) { resolvedRules[it]!! } else { it }
            } } }
            resolvedRules += next.filter { isResolved(it.toPair(), resolvedRules.keys.toList()) }
                .mapValues { (_,v) -> v.toRegex() }
            current = rules.filterNot { (k,_) -> k in resolvedRules }
        }
        return resolvedRules
    }

    private fun isResolved(rule:  Pair<String,List<List<String>>>, resolved: List<String> = emptyList()) =
        rule.second.all { p->  p.all { !it.isNumber() || it == rule.first || it in resolved } }

    private fun  List<List<String>>.toRegex() = map{ it.joinToString("") }.let {
        if(it.size ==1) { it.first() } else { it.joinToString("|","(?:",")") }
    }
    override val example = """
        42: 9 14 | 10 1
        9: 14 27 | 1 26
        10: 23 14 | 28 1
        1: "a"
        11: 42 31
        5: 1 14 | 15 1
        19: 14 1 | 14 14
        12: 24 14 | 19 1
        16: 15 1 | 14 14
        31: 14 17 | 1 13
        6: 14 14 | 1 14
        2: 1 24 | 14 4
        0: 8 11
        13: 14 3 | 1 12
        15: 1 | 14
        17: 14 2 | 1 7
        23: 25 1 | 22 14
        28: 16 1
        4: 1 1
        20: 14 14 | 1 15
        3: 5 14 | 16 1
        27: 1 6 | 14 18
        14: "b"
        21: 14 1 | 1 14
        25: 1 1 | 1 14
        22: 14 14
        8: 42
        26: 14 22 | 1 20
        18: 15 15
        7: 14 5 | 1 21
        24: 14 1
        
        abbbbbabbbaaaababbaabbbbabababbbabbbbbbabaaaa
        bbabbbbaabaabba
        babbbbaabbbbbabbbbbbaabaaabaaa
        aaabbbbbbaaaabaababaabababbabaaabbababababaaa
        bbbbbbbaaaabbbbaaabbabaaa
        bbbababbbbaaaaaaaabbababaaababaabab
        ababaaaaaabaaab
        ababaaaaabbbaba
        baabbaaaabbaaaababbaababb
        abbbbabbbbaaaababbbbbbaaaababb
        aaaaabbaabaaaaababaa
        aaaabbaaaabbaaa
        aaaabbaabbaaaaaaabbbabbbaaabbaabaaa
        babaaabbbaaabaababbaabababaaab
        aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba
    """.trimIndent()
}
private typealias Grammar = Map<String, List<List<String>>>



