package de.twittgen.aoc.y2021

import de.twittgen.aoc.Day

class Day3 : Day<Int,Int,List<String>>() {

    override val example = """
        00100
        11110
        10110
        10111
        10101
        01111
        00111
        11100
        10000
        11001
        00010
        01010
    """.trimIndent()

    override fun String.parse() = lines()

    init {
        part1(198, 2250414) {
            getGamma().let { gamma ->  gamma.toInt(2) * gamma.getEpsilon().toInt(2) }
        }
        part2(230, 6085575) {
            getRating(::getOxygenRatingFunction).toInt(2) * getRating(::getCo2RatingFunction).toInt(2)
        }
    }

    private fun List<String>.getGamma(): String {
        return (0 until this[0].length).map { index ->
            count { line -> line[index] == '1'}
        }.map {
            if(it.toDouble() >= (size /2.0)) 1 else 0
        }.joinToString("")
    }

    private fun String.getEpsilon() = map { if(it == '1') 0 else 1 }.joinToString("")

    private fun getOxygenRatingFunction(gamma: String) = { x: Char, i: Int  -> gamma[i] == x }

    private fun getCo2RatingFunction(gamma: String) = { x: Char, i: Int  -> gamma[i] != x }

    private fun List<String>.getRating(ratingFunction: (String) -> (Char, Int) -> Boolean): String {
        var remaining = this
        var index = 0
        while(remaining.size > 1) {
            remaining = remaining.filter { ratingFunction(remaining.getGamma())(it[index], index)}
            index ++
        }
        return remaining.first()
    }
}
