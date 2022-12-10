package de.twittgen.aoc.old.y2020

import de.twittgen.aoc.util.FileUtil
import org.junit.jupiter.api.Test


class day2 {
    class PwRestriction(val min: Int, val max: Int, val letter: Char )
    val pwDelimiter = Regex("(.*): ([a-z]*)")
    val restrictionPattern = Regex("(\\d+)-(\\d+) ([a-z])")
    fun pwRestriction(exp: String) : PwRestriction {
        val (min, max, letter) = restrictionPattern.matchEntire(exp)!!.destructured
        return PwRestriction(min.toInt(10),max.toInt(),letter.first())
    }
    fun PwRestriction.checkPw(pw: String): Boolean {
        return pw.count { it == letter } in min..max
    }

    fun PwRestriction.checkPwByIndex(pw: String): Boolean {
        return (pw.getOrNull(min-1) == letter) xor (pw.getOrNull(max-1) == letter)
    }

    @Test
    fun example() {
        val list = listOf(
            "1-3 a: abcde",
            "1-3 b: cdefg",
            "2-9 c: ccccccccc"
        )
        list.forEach{
            val (restrictionString, pw) =  pwDelimiter.matchEntire(it)!!.destructured
            val restriction = pwRestriction(restrictionString)
            assert(restriction.checkPw(pw))
        }
    }

    @Test
    fun part1() {
        val list = FileUtil.readInput("2020/day2").lines()
        val validPasswords = list.filter {
            val (restrictionString, pw) =  pwDelimiter.matchEntire(it)!!.destructured
            val restriction = pwRestriction(restrictionString)
            restriction.checkPw(pw)
        }
        println(validPasswords.size)
    }

    @Test
    fun part2() {
        val list = FileUtil.readInput("2020/day2").lines()
        //val list = listOf("1-3 a: abcde", "1-3 b: cdefg", "2-9 c: ccccccccc")
        val validPasswords = list.filter {
            val (restrictionString, pw) =  pwDelimiter.matchEntire(it)!!.destructured
            val restriction = pwRestriction(restrictionString)
            restriction.checkPwByIndex(pw)
        }
        println(validPasswords.size)
    }
}
