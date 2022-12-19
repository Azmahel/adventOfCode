package de.twittgen.aoc.y2020

import de.twittgen.aoc.Day
import de.twittgen.aoc.y2020.Day02.PwRestriction

private val pwDelimiter = Regex("(.*): ([a-z]*)")
private val restrictionPattern = Regex("(\\d+)-(\\d+) ([a-z])")
class Day02: Day<List<Pair<PwRestriction, String>>>() {
    override fun String.parse() = lines()
        .map {  l -> pwDelimiter.matchEntire(l)!!.destructured.let { (r, pw) -> r.toPwRestriction() to pw } }

    init {
        part1(2, 564) { it.filter { (r, pw) -> r.check(pw) }.size }
        part2(1, 325) { it.filter { (r, pw) -> r.checkByIndex(pw) }.size }
    }

    data class PwRestriction(val min: Int, val max: Int, val letter: Char ) {
        fun check(pw: String) =  pw.count { it == letter } in min..max
        fun checkByIndex(pw: String) =  (pw.getOrNull(min-1) == letter) xor (pw.getOrNull(max-1) == letter)
    }

    private fun String.toPwRestriction() = restrictionPattern.matchEntire(this)!!.destructured
        .let { (min, max, letter) -> PwRestriction(min.toInt(10),max.toInt(),letter.first()) }

    override val example = """
        1-3 a: abcde
        1-3 b: cdefg
        2-9 c: ccccccccc
    """.trimIndent()
}
