package de.twittgen.aoc.y2019.shared.util

import java.io.File
import java.lang.Exception

object FileUtil {
    fun readInput(dir: String) =
        this::class.java.getResource("/$dir/input.txt")
            ?.readText()
            ?.replace("\r\n", "\n")
            ?: "".also { println("no File for main task found")  }
        

}