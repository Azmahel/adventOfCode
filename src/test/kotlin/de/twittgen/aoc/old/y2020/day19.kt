package de.twittgen.aoc.old.y2020

import de.twittgen.aoc.util.FileUtil
import de.twittgen.aoc.util.second
import org.junit.jupiter.api.Test

class day19 {
    val input = FileUtil.readInput("2020/day19")
    val example = """
0: 4 1 5
1: 2 3 | 3 2
2: 4 4 | 5 5
3: 4 5 | 5 4
4: "a"
5: "b"

ababbb
bababa
abbbab
aaabbb
aaaabbb
    """.trimIndent()

    val example2 = """
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
    fun parseInput(s: String): Pair<Map<String, List<List<String>>>, List<String>> {
        val parts = s.lines()
        val rules = parts.takeWhile { it.isNotEmpty() }
            .map {with(
                it
                    .replace("\"","")
                    .split(":")
            ) {
                first() to second()
                    .split("|")
                    .map {
                        it.split(" ")
                            .filter { it.isNotBlank() }
                    }
            }
            }
        val inputs = parts.takeLastWhile { it.isNotEmpty() }
        return rules.toMap() to inputs
    }

    @Test
    fun example() {
        val (rules, inputs) = parseInput(example)
        val regex = resolveRules(rules)["0"]!!.toRegex()
        val result = inputs.filter { regex.matches(it) }
        assert(
            result == listOf(
                "ababbb",
                "abbbab"
            )
        )
    }

    @Test
    fun part1() {
        val (rules, inputs) = parseInput(input)
        val regex = resolveRules(rules)["0"]!!.toRegex()
        val result = inputs.filter { regex.matches(it) }
        println(result.size)
    }

    @Test
    fun part2() {
        var (oldrules, inputs) = parseInput(input)
        //inputs =  listOf("bbbbbbbaaaabbbbaaabbabaaa","aaaaabbaabaaaaababaa")
        val rules = oldrules.toMutableMap().apply {
            set("8", listOf(
                listOf("42"),
                listOf("42", "8")
            ))
            set("11", listOf(
                listOf("42", "31"),
                listOf("42", "11", "31" )
            ))
        }
        val resolved = resolveRules(rules)
        val eight = resolved["8"]!!.replace("8","+")
        //assuming no deeper loop than 20
        val eleven = (1..20).map {
            "(?:${resolved["42"]}){$it}(?:${resolved["31"]}){$it}"
        }.joinToString("|","(?:", ")")
        val regex = resolved["0"]!!
            .replace("8",eight)
            .replace("11",eleven )
            .toRegex()

        val result = inputs.filter { regex.matches(it)}
        println(result.size)
    }

    private fun and(a: Boolean, b: Boolean) = a && b

    fun resolveRules(
        rules: Map<String, List<List<String>>>,
        possibleLoops: Boolean = false
    ): MutableMap<String, String> {
        val resolvedRules = rules
            .filter { isResolved(it.toPair()) }
            .mapValues { (_,v) -> v.toRegex() }
            .toMutableMap()
        var current = rules.filterNot { (k,_) -> k in resolvedRules }
        while(current.isNotEmpty()) {
            val next = current.mapValues { (_,v) ->
                v.map {
                    it.map {
                        if(it in resolvedRules.keys) {
                            resolvedRules[it]!!
                        } else {
                            it
                        }
                    }
                }.apply {

                }
            }
            resolvedRules += next
                .filter { isResolved(it.toPair(), resolvedRules.keys.toList()) }
                .mapValues { (_,v) -> v.toRegex() }
            current = rules.filterNot { (k,_) -> k in resolvedRules }
        }
        return resolvedRules
    }

    fun isResolved(rule:  Pair<String,List<List<String>>>, resolved: List<String> = emptyList()): Boolean {
        return rule.second.all {
            it.all {
                it.toIntOrNull() == null
                        || it == rule.first
                        || it in resolved
            }
        }
    }
}

private fun  List<List<String>>.toRegex(): String {
   return map{
       it.joinToString("")
   }.let {
       if(it.size ==1) {
           it.first()
       } else {
           it.joinToString("|","(?:",")")
       }
   }
}

