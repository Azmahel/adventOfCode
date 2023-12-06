package de.twittgen.aoc.util

import kotlin.math.pow
import kotlin.math.sqrt

fun pq(p: Double, q: Double): Pair<Double, Double> {
    val pHalve = p / 2.0
    val sqrt = sqrt(pHalve.pow(2) - q)
    return Pair(-pHalve - sqrt, -pHalve + sqrt)
}
