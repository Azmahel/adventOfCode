package de.twittgen.aoc.util

import de.twittgen.aoc.util.NestedList.Nested
import de.twittgen.aoc.util.NestedList.Terminal
import java.lang.IllegalStateException


private val findTerminalPair = Regex(".*<([^<>]*)>.*")
sealed class NestedList {
    data class Terminal(val value: Int): NestedList()
    data class Nested(val content: List<NestedList>) : NestedList()
}


tailrec fun String.toNestedList(map: Mapping<String, NestedList> = Mapping(alphabet.map(Char::toString))) : Nested {
    if(length == 1) return map[this]!! as Nested
    val target = findTerminalPair.matchEntire(this)!!.groupValues.drop(1).first()
    return replace("<$target>", map.put(Nested(target.split(',').map { it.resolveToNestedList(map) })))
        .toNestedList(map)
}


private fun String.resolveToNestedList(map: Mapping<String, NestedList>) =
    toIntOrNull()?.let { Terminal(it) } ?: map[this] ?: if(isEmpty()) Nested(emptyList()) else throw IllegalStateException()


