package de.twittgen.aoc.util
const val emptyLine = "\n\n"
fun String.toIntcodeProgram() =  split(",").map { it.toInt(10) }
fun String.toIntRange(delimiter: String = "-") = split(delimiter).map { it.toInt(10) }.let { rangeOf(it[0], it[1]) }
fun String.isLowerCase() = lowercase() == this
fun String.isNumber() = toIntOrNull() != null
val alphabet = ('a'..'z')+('A'..'Z')
fun String.second(): Char = toList().second()
fun String.digitsToInt() = map { it.digitToInt() }
fun String.toPoint3d(): Point3D = split(',').map(String::toInt).let { (x,y,z) ->Point3D(x,y,z) }
fun String.toPoint2d(): Point2D = split(',').map(String::toInt).let { (x,y) ->Point2D(x,y) }

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
fun Regex.firstMatch(s: String) = matchEntire(s)?.destructured?.component1()

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