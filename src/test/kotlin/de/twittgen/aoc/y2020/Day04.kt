package de.twittgen.aoc.y2020

import de.twittgen.aoc.Day

class Day04 : Day<List<Passport>>() {

    override fun String.parse() = split("\n\n").map { passport ->
        passport.lines().flatMap { it.split(" ") }.associate {
            Regex("(.*):(.*)").matchEntire(it)!!.destructured.let { (k,v) -> k to v }
        }
    }

    init {
        part1(2, 239) { it.count { p ->  p.checkRequiredFields() } }
        part2(2, 188) { it.count { p -> p.validateFields() } }
    }

    private fun Passport.checkRequiredFields() = requiredFields.all { keys.contains(it) }
    private fun Passport.validateFields() = fieldConstraints.all{ (key, predicate) -> get(key)?.predicate() ?: false }

    private val eyeColors = listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
    private val passportIdPattern = Regex("[0-9]{9}")
    private val validBirthYears = 1920..2002
    private val validIssueYears = 2010..2020
    private val validHeights = 2020..2030
    private val isValidHeight: String.() -> Boolean = { when {
        endsWith("cm") -> removeSuffix("cm").toInt() in 150..193
        endsWith("in") -> removeSuffix("in").toInt() in 59..76
        else -> false
    } }
    private val hairColorPattern = Regex("#[0-9a-f]{6}")
    private val requiredFields = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
    private val fieldConstraints: List<Pair<String, String.()->Boolean> > = listOf(
        "byr" to { toInt() in validBirthYears },
        "iyr" to { toInt() in validIssueYears },
        "eyr" to { toInt() in validHeights },
        "hgt" to isValidHeight,
        "hcl" to { matches(hairColorPattern) },
        "ecl" to { this in eyeColors },
        "pid" to { matches(passportIdPattern) }
    )

    override val example = """
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
}

private typealias Passport = Map<String,String>
