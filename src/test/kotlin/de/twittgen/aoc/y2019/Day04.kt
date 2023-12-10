package de.twittgen.aoc.y2019

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.digits
import de.twittgen.aoc.util.toIntRange

class Day04 : Day<IntRange>() {
    override fun String.parse() = toIntRange()

    init {
        part1(null, 1729) {
            it.getPasswordsInRange(numbersAreRising, hasIdenticalAdjacentNumbers).size
        }
        part2(null, 1172) {
            it.getPasswordsInRange(numbersAreRising, hasTwoNumberRun).size
        }
    }

    private fun IntRange.getPasswordsInRange(vararg constraints: Constraint) =
        filter {int -> constraints.all{ constraint -> int.toString().constraint() } }



    private val doubleNumber = Regex(".*(00|11|22|33|44|55|66|77|88|99).*")
    private val hasIdenticalAdjacentNumbers: Constraint = { doubleNumber.matches(this) }
    private val numbersAreRising: Constraint = { toInt().let { it.digits == it.digits.sorted() }}
    private val hasTwoNumberRun: Constraint = { (0..9).map{ it.twoNumberRunRegex() }.any{ it.matches(this) } }
    private fun Int.twoNumberRunRegex() =  Regex(".*(?<!$this)($this){2}(?!$this).*")
}
private typealias Constraint = String.() -> Boolean