package de.twittgen.aoc.util

fun <T> List<T>.second() = get(1)
fun <T> List<T>.secondOrNull() = getOrNull(1)

operator fun <T> List<T>.times(times: Int): List<T> = (1..times).flatMap { this }
fun List<Int>.product() = foldRight(1) { a,b -> a * b }
fun <T> List<List<T>>.column(i: Int) = map { it[i] }
fun <T> List<List<T>>.columns() = (0..get(0).lastIndex).map { column(it) }
fun <T> List<T>.takeUntil(predicate: (T)-> Boolean): List<T> =
    firstOrNull(predicate) ?.let { takeWhile{ x -> !predicate(x) } + it } ?: takeWhile{ !predicate(it) }
