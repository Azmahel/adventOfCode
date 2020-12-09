package de.twittgen.aoc.y2019.shared.util

val Int.digits
    get() = toString().map{ it.toString().toInt() }