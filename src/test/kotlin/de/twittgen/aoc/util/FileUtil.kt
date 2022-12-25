package de.twittgen.aoc.util

object FileUtil {
    fun readInput(dir: String) =
        this::class.java.getResource("/$dir/input.txt")
            ?.readText()
            ?.replace("\r\n", "\n")
            ?: "".also { println("no File for main task found")  }
}