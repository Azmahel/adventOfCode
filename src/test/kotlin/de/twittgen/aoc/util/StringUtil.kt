package de.twittgen.aoc.util

fun String.toIntcodeProgram() =  split(",").map { it.toInt(10) }
fun String.toIntRange() = split("-").map { it.toInt(10) }.let { it[0]..it[1] }
fun String.isLowerCase() = lowercase() == this