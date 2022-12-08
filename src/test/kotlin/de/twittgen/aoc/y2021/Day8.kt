package de.twittgen.aoc.y2021

import de.twittgen.aoc.util.FileUtil
import de.twittgen.aoc.util.second
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day8 {
    val input by lazy { FileUtil.readInput("2021/day8").parse() }
    val example = """be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce""".parse()

    private fun String.parse() =
        lines()
            .map{ line ->
                line
                    .split(" | ")
                    .map { part -> part.split(" ").map { it.toSet() } }
            }.map {
                it.first() to it.second()
            }

    private fun Set<Char>.is1() = size==2
    private fun Set<Char>.is4() = size==4
    private fun Set<Char>.is7() = size==3
    private fun Set<Char>.is8() = size==7

    private fun Set<Char>.is3(seven: Set<Char>) = containsAll(seven) && minus(seven).size == 2
    private fun Set<Char>.is9(three: Set<Char>) = containsAll(three) && minus(three).size == 1

    private fun Set<Char>.is5(b: Char) = contains(b) && minus(b).size == 4
    private fun Set<Char>.is6(five: Set<Char>, three: Set<Char>) =
        containsAll(five) && minus(five).size == 1 && !is9(three)

    private fun List<Set<Char>>.getDecoding(): Map<Set<Char>, Char> {
        val result = mutableMapOf<Int, Set<Char>>()
        result[1] = first { it.is1() }
        result[4] = first { it.is4() }
        result[7] = first { it.is7() }
        result[8] = first { it.is8() }

        result[3] = first { it.is3(result[7]!!) }
        result[9] = first { it.is9(result[3]!!) }

        val b = (result[9]!! - result[3]!!).first()

        result[5] = first { it.is5(b) }
        result[6] = first { it.is6(result[5]!!, result[3]!!) }

        val remaining = filterNot { it in result.values }

        result[2] = remaining.first { it.size == 5 }
        result[0] = remaining.first { it.size == 6 }
        return result.map { (k,v) -> v to "$k".first() }.toMap()
    }

    private fun List<Set<Char>>.decode(key: Map<Set<Char>, Char>) = map { key[it]!! }.joinToString("")

    @Test
    fun example() {
        val result = example.sumOf {
            it.second.count { i ->
                with(i){ is1() || is4() || is7() || is8()
                }
            }
        }
        assertEquals(26, result)
    }

    @Test
    fun example2() {
        val result = example.map { (sequence, cypher) ->
            cypher.decode(sequence.getDecoding()).toInt()
        }
        assertEquals(61229, result.sum())
    }

    @Test
    fun part1() {
        val result = input.sumOf {
            it.second.count { i ->
                with(i){ is1() || is4() || is7() || is8()
                }
            }
        }
        println(result)
    }

    @Test
    fun part2() {
        val result = input.map { (sequence, cypher) ->
            cypher.decode(sequence.getDecoding()).toInt()
        }
        println(result.sum())
    }
}
