package de.twittgen.aoc.y2020

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.emptyLine
import de.twittgen.aoc.util.groups
import de.twittgen.aoc.util.second
import de.twittgen.aoc.util.toIntRange

class Day16: Day<Triple<List<FieldConstraint>, Ticket, List<Ticket>>>(){
    private val fieldExp = Regex("([a-z ]+): (\\d+-\\d+) or (\\d+-\\d+)")
    override fun String.parse() = split(emptyLine).let { (con, ticket, o) ->
        val req = con.lines().map { fieldExp.groups(it)!!.let { (title, a, b) ->
            title to listOf(a.toIntRange("-"),b.toIntRange("-"))
        }}
        val my = ticket.lines().second().split(",").map { it.toInt() }
        val other = o.lines().drop(1).map { t ->  t.split(",").map { it.toInt() } }
        Triple(req,my,other)
    }

    init {
        part1(71, 25961) { (req, _, other) -> other.getInvalidFields(req).sum() }
        part2(1, 603409823791) { (req, my, other) ->
            val validOther = other.filter { t -> t.getInvalidField(req).isEmpty() }
            val myTranslated = my.translate(req, validOther)
            myTranslated.filter {f -> f.second.contains("departure") }
                .fold(1L) { acc, pair -> acc * pair.first }
        }
    }

    private fun List<Ticket>.getInvalidFields(requirements: List<FieldConstraint>) =
        flatMap { ticket -> ticket.getInvalidField(requirements) }


    private fun Ticket.getInvalidField(requirements: List<FieldConstraint>) =  filterNot { field ->
        requirements.any { req -> req.second.any { range ->
            field in range
        } }
    }

    private fun Ticket.translate(requirements: List<FieldConstraint>, other: List<Ticket>): List<Pair<Int, String>> {
        val allTickets  = other + listOf(this)
        var possibleTranslations = mapIndexed { index, value ->
            val otherValues = allTickets.map { it[index] }
            value to requirements.filter { field -> otherValues.all { field.second.any { range ->
                it in range
            } } }.map { it.first }
        }
        val translated = possibleTranslations.filter { it.second.size ==1 }.map{ (x,y) -> x to y.first() }.toMutableList()
        while(possibleTranslations.isNotEmpty()) {
            possibleTranslations = possibleTranslations.map { (x,y) -> x to y.minus(translated.map { it.second }.toSet()) }
                .filterNot { it.second.isEmpty() }
            translated += possibleTranslations.filter { it.second.size ==1 }.map{ (x,y) -> x to y.first() }
        }
        return translated
    }

    override val example = """
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
}
private typealias FieldConstraint = Pair<String,List<IntRange>>
private typealias Ticket = List<Int>