package de.twittgen.aoc.y2023

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.times

class Day01 : Day<List<String>>() {
    override fun String.parse() = lines()

    init {
        part1(null, 55816) { it.sumOf { value -> value.findFirstAndLasDigit() } }
        part2(281, 54980) {
            it.sumOf { value -> value.findFirstAndLastDigitOrNumberWords() }
        }
    }

    private fun String.findFirstAndLastDigitOrNumberWords() = (
            firstAndLastNumber.matchEntire(this)?.destructured?.toList()
                ?: singleNumber.matchEntire(this)!!.destructured.toList().times(2)
            ).let { (a, b) -> "${numberWords.getOrDefault(a,a)}${numberWords.getOrDefault(b,b)}".toInt() }

    private fun String.findFirstAndLasDigit() = filter { c -> c.isDigit() }.let { digits -> "${digits.first()}${digits.last()}".toInt() }
    private val numberWords = mapOf("one" to "1", "two" to "2", "three" to "3", "four" to "4", "five" to "5", "six" to "6", "seven" to "7", "eight" to "8", "nine" to "9",)
    private val numberOrWord = "0|${numberWords.values.joinToString("|")}|${numberWords.keys.joinToString("|")}"
    private val firstAndLastNumber = Regex(".*?($numberOrWord).*($numberOrWord).*")
    private val singleNumber = Regex(".*?($numberOrWord).*")
    override val example = """
        two1nine
        eightwothree
        abcone2threexyz
        xtwone3four
        4nineeightseven2
        zoneight234
        7pqrstsixteen
    """.trimIndent()
}