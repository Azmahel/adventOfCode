package de.twittgen.aoc.y2019.shared.util

fun String.toIntcodeProgram() =  split(",").map { it.toInt(10) }
fun String.toIntRange() = split("-").map { it.toInt(10) }.let { it[0]..it[1] }
