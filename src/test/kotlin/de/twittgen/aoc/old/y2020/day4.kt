package de.twittgen.aoc.old.y2020

import de.twittgen.aoc.util.FileUtil
import org.junit.jupiter.api.Test

 private typealias Passport = Map<String,String>
class day4 {
    val input = FileUtil.readInput("2020/day4")
    val example = """
        ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
        byr:1937 iyr:2017 cid:147 hgt:183cm

        iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
        hcl:#cfa07d byr:1929

        hcl:#ae17e1 iyr:2013
        eyr:2024
        ecl:brn pid:760753108 byr:1931
        hgt:179cm

        hcl:#cfa07d eyr:2025 pid:166559648
        iyr:2011 ecl:brn hgt:59in
    """.trimIndent()

    fun readInput(s: String): List<Passport> {
       return s.replace("\r","").split("\n\n").map { passport ->
            val entries = passport.lines().flatMap { it.split(" ")}
                entries.associate {
                    val (key, value) = Regex("(.*):(.*)").matchEntire(it)!!.destructured
                key to value
            }
        }

    }

    @Test
    fun example() {
        val passports = readInput(example)
        val requiredFields = listOf(
            "byr",
            "iyr",
            "eyr",
            "hgt",
            "hcl",
            "ecl",
            "pid"
        )
        val validity = passports.map { it.checkFieldPresence(requiredFields) }
        assert (validity.count { it } == 2)
    }

    @Test
    fun part1() {
        val passports = readInput(input)
        val requiredFields = listOf(
            "byr",
            "iyr",
            "eyr",
            "hgt",
            "hcl",
            "ecl",
            "pid"
        )
        val validity = passports.map { it.checkFieldPresence(requiredFields) }
        println(validity.count { it })
    }
    @Test
    fun part2() {
        val passports = readInput(input)
        val requiredFields = listOf(
            "byr" to isValidBirthYear,
            "iyr" to isValidIssueYear,
            "eyr" to isValidExpYear,
            "hgt" to isValidHeight,
            "hcl" to isValidHairColor,
            "ecl" to isValidEyeColor,
            "pid" to isValidPassPortID
        )
        val validity = passports.map { it.validateFields(requiredFields) }
        println(validity.count { it })
    }

    fun Passport.checkFieldPresence(fields: List<String>) = fields.all {
        keys.contains(it)
    }

    fun Passport.validateFields(requirements: List<Pair<String,String.()->Boolean>>) : Boolean {
        val result = requirements.all{ (key, predicate) ->
            val x = get(key)?.predicate() ?: false
            x
        }
        return result
    }

    val isValidBirthYear: String.() -> Boolean = { toInt() in 1920..2002 }
    val isValidIssueYear: String.() -> Boolean = { toInt() in 2010..2020 }
    val isValidExpYear: String.() -> Boolean = { toInt() in 2020..2030 }
    val isValidHeight: String.() -> Boolean = { when {
        endsWith("cm") -> removeSuffix("cm").toInt() in 150..193
        endsWith("in") -> removeSuffix("in").toInt() in 59..76
        else -> false
    } }
    val isValidHairColor: String.() -> Boolean = { matches(Regex("#[0-9a-f]{6}")) }
    val isValidEyeColor: String.() -> Boolean = { this in listOf(
        "amb",
        "blu",
        "brn",
        "gry",
        "grn",
        "hzl",
        "oth"
    )
    }
    val isValidPassPortID: String.() -> Boolean = { matches(Regex("[0-9]{9}")) }
}