package de.twittgen.aoc.util

val Number.digits get() = toString().digitsToInt()
fun rangeOf(a: Int, b: Int) =  if (b > a) a..b else b..a