package de.twittgen.aoc.y2019.day4

import de.twittgen.aoc.y2019.shared.util.digits

object PasswordUtil {

    fun getPasswordsInRange(range: IntRange, constraints: List<Int.() -> Boolean>): List<Int> {
        return range.filter {int -> constraints.all{ constraint -> int.constraint() } }

    }

    val isSixDigitNumber : Int.() -> Boolean =  {Regex("[0-9]{6}").matches(toString()) }
    val hasIdenticalAdjacentNumbers: Int.() -> Boolean = { Regex(".*(00|11|22|33|44|55|66|77|88|99).*").matches(toString()) }
    val numbersAreRising: Int.() -> Boolean = { digits == digits.sorted() }
    val hasTwoNumberRun: Int.() -> Boolean = { (0..9).map{ it.twoNumberRunRegex() }.any{ it.matches(toString()) } }

    private val twoNumberRunRegex: Int.() -> Regex = { Regex(".*(?<!$this)($this){2}(?!$this).*")}
}