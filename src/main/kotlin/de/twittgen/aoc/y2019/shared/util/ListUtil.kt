package de.twittgen.aoc.y2019.shared.util

fun <T> List<T>.second() = get(1)

operator fun <T> List<T>.times(times: Int): List<T> = (1..times).flatMap { this }