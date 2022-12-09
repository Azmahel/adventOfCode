package de.twittgen.aoc.util


class  Mapping<K,V>( keys : List<K>) {
    private val placeholders = ArrayDeque(keys)
    private val map = mutableMapOf<K,V>()
    fun put(i: V) = placeholders.removeFirst().also { map[it] = i }
    operator fun get(i : K) = map[i]
}
