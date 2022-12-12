package de.twittgen.aoc.util

val Number.digits
    get() = toString().map{ it.toString().toInt() }