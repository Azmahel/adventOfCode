package de.twittgen.aoc.util

import java.lang.Math.pow
import kotlin.math.abs

val Number.digits get() = toString().digitsToInt()
fun rangeOf(a: Int, b: Int) =  if (b > a) a..b else b..a
fun List<Int>.mean() = sum().toDouble() / size
fun List<Int>.variance(measure :List<Int>.() -> Double = { mean()}) =
    measure().let{ m -> sumOf { pow(abs(it - m),2.0) }.toDouble() / (size -1) }