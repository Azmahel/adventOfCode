package de.twittgen.aoc.current.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.second

 private typealias DecodingEntry = Pair<List<Set<Char>>, List<Set<Char>>>

class Day8 : Day<List<DecodingEntry>>() {
    override fun String.parse(): List<DecodingEntry> = lines()
        .map { line ->
            line.split(" | ")
                .map { part -> part.split(" ").map { it.toSet() } }
                .run { first() to second() }
        }

    init {
        part1(26, 387) {
            sumOf { it.second.count { i -> with(i) { is1() || is4() || is7() || is8() } } }
        }

        part2(61229, 986034) {
            sumOf { (sequence, cypher) -> cypher.decode(sequence.getDecoder()).toInt() }
        }
    }

    private fun Set<Char>.is1() = size == 2
    private fun Set<Char>.is4() = size == 4
    private fun Set<Char>.is7() = size == 3
    private fun Set<Char>.is8() = size == 7
    private fun Set<Char>.is3(decoded: Map<Int, Set<Char>>) = containsAll(decoded[7]!!) && minus(decoded[7]!!).size == 2
    private fun Set<Char>.is9(decoded: Map<Int, Set<Char>>) = containsAll(decoded[3]!!) && minus(decoded[3]!!).size == 1
    private fun Set<Char>.is5(b: Char) = contains(b) && minus(b).size == 4
    private fun Set<Char>.is6(decoded: Map<Int, Set<Char>>) =
        containsAll(decoded[5]!!) && minus(decoded[5]!!).size == 1 && !is9(decoded)

    private fun List<Set<Char>>.getDecoder(): Map<Set<Char>, Char> {
        val result = mutableMapOf<Int, Set<Char>>()
        result[1] = first { it.is1() }
        result[4] = first { it.is4() }
        result[7] = first { it.is7() }
        result[8] = first { it.is8() }
        result[3] = first { it.is3(result) }
        result[9] = first { it.is9(result) }
        result[5] = first { it.is5((result[9]!! - result[3]!!).first()) }
        result[6] = first { it.is6(result) }
        val remaining = filterNot { it in result.values }
        result[2] = remaining.first { it.size == 5 }
        result[0] = remaining.first { it.size == 6 }
        return result.map { (k, v) -> v to "$k".first() }.toMap()
    }

    private fun List<Set<Char>>.decode(key: Map<Set<Char>, Char>) = map { key[it]!! }.joinToString("")

    override val example = """
        be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
        edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
        fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
        fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
        aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
        fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
        dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
        bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
        egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
        gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
    """.trimIndent()
}
