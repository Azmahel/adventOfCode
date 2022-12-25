package de.twittgen.aoc.util

fun <T> List<T>.second() = get(1)
fun <T> List<T>.secondOrNull() = getOrNull(1)
fun <T> List<T>.middle() = get((size-1) /2)
fun <T> List<T>.hasDuplicate() = toSet().size != size


operator fun <T> List<T>.times(times: Int): List<T> = (1..times).flatMap { this }
fun List<Int>.product() = foldRight(1) { a,b -> a * b }
fun <T> List<List<T>>.column(i: Int) = map { it[i] }
fun <T> List<List<T>>.columns() = (0..get(0).lastIndex).map { column(it) }
fun <T> List<T>.takeUntil(predicate: (T)-> Boolean): List<T> =
    firstOrNull(predicate)?.let { takeWhile{ x -> !predicate(x) } + it } ?: takeWhile{ !predicate(it) }
fun <T> List<T>.mapIf(predicate: (T) -> Boolean, transform: (T) -> T): List<T> = map { if(predicate(it)) transform(it) else it }
fun <T> ofLength(i: Int, supplier: (Int)->T) = (0 until i).map { supplier(it) }

fun<T,R> Triple<T, T, T>.forEach(m:(T)-> (R)) = Triple(m(first), m(second), m(third))
fun <T>MutableList<T>.replaceAt(i: Int, v :T)= apply { set(i,v) }
infix fun <T> Iterable<T>.containsAll(other: Iterable<T>) = intersect(other.toSet()) == toSet()
fun <T,R> List<Pair<T,R>>.toPairOfLists() = map { it.first } to map { it.second }
fun <T> List<T>.cycle() = drop(1) + first()
fun <T> MutableList<T>.cycle() { add(removeAt(0)) }
fun <T> Iterable<T>.boundaries() = listOf(first(), last())
fun <T>List<Pair<T,T>>.filterMirrors(): List<Pair<T, T>> {
    val added = mutableSetOf<Pair<T,T>>()
    return asSequence().filterNot { (a,b) -> a to b in added || b to a in added }.map { it.also { added += it } }.toList()
}