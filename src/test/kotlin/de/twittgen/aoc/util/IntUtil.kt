package de.twittgen.aoc.util

import kotlin.math.sign

val Number.digits
    get() = toString().map{ it.toString().toInt() }

fun rangeOf(a: Int, b: Int) =  if (b > a) a..b else b..a