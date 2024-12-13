package de.twittgen.aoc.util

import kotlin.math.pow
import kotlin.math.sqrt

fun pq(p: Double, q: Double): Pair<Double, Double> {
    val pHalve = p / 2.0
    val sqrt = sqrt(pHalve.pow(2) - q)
    return Pair(-pHalve - sqrt, -pHalve + sqrt)
}
tailrec fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
fun lcm(a: Long, b: Long) = a * b / gcd(a, b)
fun lcm(values: List<Long>) =values.reduce{ a, b -> lcm(a, b) }

fun Double.isWhole() =  this - this.toLong() == 0.0