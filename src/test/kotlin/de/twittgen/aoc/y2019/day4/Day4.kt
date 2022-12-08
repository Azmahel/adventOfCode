package de.twittgen.aoc.y2019.day4

import de.twittgen.aoc.y2019.day4.PasswordUtil.hasIdenticalAdjacentNumbers
import de.twittgen.aoc.y2019.day4.PasswordUtil.hasTwoNumberRun
import de.twittgen.aoc.y2019.day4.PasswordUtil.isSixDigitNumber
import de.twittgen.aoc.y2019.day4.PasswordUtil.numbersAreRising
import de.twittgen.aoc.util.toIntRange
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day4 {
    @Test
    fun part1() {
        val constraints = listOf(
            isSixDigitNumber,
            numbersAreRising,
            hasIdenticalAdjacentNumbers
        )

        val passwords = PasswordUtil.getPasswordsInRange("153517-630395".toIntRange(), constraints)

        println(passwords.size)
        assertEquals(1729,passwords.size)
    }

    @Test
    fun part1Examples() {
        val constraints = listOf(
            isSixDigitNumber,
            numbersAreRising,
            hasIdenticalAdjacentNumbers
        )

        assertEquals(listOf(111111),  PasswordUtil.getPasswordsInRange("111111-111111".toIntRange(), constraints) )
        assertEquals(emptyList<Int>(),  PasswordUtil.getPasswordsInRange("223450-223450".toIntRange(), constraints) )
        assertEquals(emptyList<Int>(),  PasswordUtil.getPasswordsInRange("123789-123789".toIntRange(), constraints) )
    }

    @Test
    fun part2() {
        val constraints = listOf(
            isSixDigitNumber,
            numbersAreRising,
            hasTwoNumberRun
        )

        val passwords = PasswordUtil.getPasswordsInRange("153517-630395".toIntRange(), constraints)

        println(passwords.size)
        assertEquals(1172,passwords.size)
    }

    @Test
    fun part2Examples() {
        val constraints = listOf(
            isSixDigitNumber,
            numbersAreRising,
            hasTwoNumberRun
        )

        assertEquals(listOf(112233),  PasswordUtil.getPasswordsInRange("112233-112233".toIntRange(), constraints) )
        assertEquals(emptyList<Int>(),  PasswordUtil.getPasswordsInRange("123444-123444".toIntRange(), constraints) )
        assertEquals(listOf(111122),  PasswordUtil.getPasswordsInRange("111122-111122".toIntRange(), constraints) )
    }
}