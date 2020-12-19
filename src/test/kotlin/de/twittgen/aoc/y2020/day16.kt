package de.twittgen.aoc.y2020

import de.twittgen.aoc.y2019.shared.util.FileUtil
import de.twittgen.aoc.y2019.shared.util.second
import org.junit.jupiter.api.Test
import java.io.File

typealias FieldConstraint = Pair<String,List<IntRange>>
typealias Ticket = List<Int>
class day16 {
    val input = FileUtil.readInput("2020/day16")
    val example = """
        class: 1-3 or 5-7
        row: 6-11 or 33-44
        seat: 13-40 or 45-50

        your ticket:
        7,1,14

        nearby tickets:
        7,3,47
        40,4,50
        55,2,20
        38,6,12
    """.trimIndent()

    val example2 = """
        class: 0-1 or 4-19
        row: 0-5 or 8-19
        seat: 0-13 or 16-19

        your ticket:
        11,12,13

        nearby tickets:
        3,9,18
        15,1,5
        5,14,9
    """.trimIndent()

    val fieldExp = Regex("([a-z ]+): (\\d+)-(\\d+) or (\\d+)-(\\d+)")
    fun parseInput(s: String): Triple<List<FieldConstraint>, Ticket, List<Ticket>> {
         with(s.replace("\r","").split("\n\n")) {
            val req = first().lines().map {
                val(title, a,b,x,y) = fieldExp.matchEntire(it)!!.destructured
                title to listOf((a.toInt()..b.toInt()), (x.toInt()..y.toInt()))
            }
            val my = second().lines().second().split(",").map { it.toInt() }
            val other = get(2).lines().drop(1).map { it.split(",").map { it.toInt() } }
            return Triple(req,my,other)
        }
    }


    @Test
    fun example() {
        val (requirements, my, other) = parseInput(example)
        val result = other.getInvalidFields(requirements).sum()
        assert(
            result == 71
        )
    }

    @Test
    fun part1() {
        val (requirements, my, other) = parseInput(input)
        val result = other.getInvalidFields(requirements).sum()
        println(result)
    }

    @Test
    fun part2() {
        val (requirements, my, other) = parseInput(input)
        val validOther = other.filter { it.getInvalidField(requirements).isEmpty() }
        val myTranslated = my.translate(requirements, validOther)

        println(
           myTranslated.filter { it.second.contains("departure") }.fold(1L) { acc, pair ->   acc * pair.first }
       )

    }


    fun List<Ticket>.getInvalidFields(requirements: List<FieldConstraint>): List<Int> {
        return flatMap { ticket ->
            ticket.getInvalidField(requirements)
        }
    }

    fun Ticket.getInvalidField(requirements: List<FieldConstraint>): List<Int> {
        return filterNot { field ->
            requirements.any { req ->
                req.second.any { range ->
                    field in range
                }
            }
        }
    }

    @Test
    fun readFile() {
        val text = File("C:\\Users\\trist\\Documents\\cyoa\\OEBPS\\chapter001").readText()
        val x =0
    }
}

private fun Ticket.translate(requirements: List<FieldConstraint>, other: List<Ticket>): List<Pair<Int, String>> {
    val tickets: List<Ticket> = other + listOf(this)
    var possibleTranslations = mapIndexed { index, value ->
        val otherValues = tickets.map { it[index] }
        value to requirements.filter { field ->
           otherValues.all {
               field.second.any { range ->
                   it in range
               }
           }
        }.map { it.first }
    }
    val translated = possibleTranslations.filter { it.second.size ==1 }.map{ (x,y) -> x to y.first() }.toMutableList()
    while(possibleTranslations.isNotEmpty()) {
        possibleTranslations = possibleTranslations.map { (x,y) -> x to y.minus(translated.map { it.second }) }.filterNot { it.second.isEmpty() }
        translated += possibleTranslations.filter { it.second.size ==1 }.map{ (x,y) -> x to y.first() }
    }
    return translated

}
