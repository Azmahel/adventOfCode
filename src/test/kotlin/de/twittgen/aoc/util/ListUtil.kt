package de.twittgen.aoc.util

fun <T> List<T>.second() = get(1)
fun <T> List<T>.secondOrNull() = getOrNull(1)
fun <T> List<T>.middle() = get((size-1) /2)
fun <T> List<T>.filterIn(other: Collection<T>) = filter { it in other }
fun <T> List<T>.hasDuplicate() = toSet().size != size


operator fun <T> List<T>.times(times: Int): List<T> = (1..times).flatMap { this }
fun List<Int>.product() = foldRight(1) { a,b -> a * b }
fun <T> List<List<T>>.column(i: Int) = map { it[i] }
fun <T> List<List<T>>.columns() = (0..get(0).lastIndex).map { column(it) }
fun <T> List<T>.takeUntil(predicate: (T)-> Boolean): List<T> =
    firstOrNull(predicate) ?.let { takeWhile{ x -> !predicate(x) } + it } ?: takeWhile{ !predicate(it) }

fun<T,R> Triple<T, T, T>.forEach(m:(T)-> (R)) = Triple(m(first), m(second), m(third))