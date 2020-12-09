package de.twittgen.aoc.y2019.shared.util

object FileUtil {
    fun readInput(dir: String) = this::class.java.getResource("/$dir/input.txt").readText()
}