package de.twittgen.aoc.util

typealias IntCodeProgram = List<Int>
fun String.toIntCodeProgram() =  split(",").map { it.toInt(10) }
