package de.twittgen.aoc.util

fun String.toIntcodeProgram() =  split(",").map { it.toInt(10) }
fun String.toIntRange(delimiter: String = "-") = split(delimiter).map { it.toInt(10) }.let { it[0]..it[1] }
fun String.isLowerCase() = lowercase() == this
fun String.isNumber() = toIntOrNull() != null
val alphabet = ('a'..'z')+('A'..'Z')

fun String.takePartitioning(vararg m : (Char) -> Boolean): List<String> {
    var remainder = this
    val result = mutableListOf<String>()
    for (n in m) {
        val v = remainder.takeWhile(n)
        result.add(v)
        remainder = remainder.drop(v.length)
    }
    return result + remainder
}

fun String.takeLastPartitioning(vararg m : (Char) -> Boolean): List<String> {
    var remainder = this
    val result = mutableListOf<String>()
    for (n in m) {
        val v = remainder.takeLastWhile(n)
        result.add(v)
        remainder = remainder.dropLast(v.length)
    }
    return (result + remainder)
}