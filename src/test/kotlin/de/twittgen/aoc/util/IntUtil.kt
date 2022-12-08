package de.twittgen.aoc.util

val Int.digits
    get() = toString().map{ it.toString().toInt() }