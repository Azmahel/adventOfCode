package de.twittgen.aoc.util

fun <T> T.repeat(times: Int, block: (T)->T ): T {
    var current = this
    kotlin.repeat(times) { current = block(current) }
    return current
}