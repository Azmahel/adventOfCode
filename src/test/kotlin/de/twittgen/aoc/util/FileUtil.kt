package de.twittgen.aoc.util

import io.github.rybalkinsd.kohttp.dsl.httpGet
import io.github.rybalkinsd.kohttp.ext.url
import java.io.File


object FileUtil {
    fun readInput(dir: String) =
        File("./src/test/resources/$dir/input.txt").let {
            if(!it.exists()) null.also { println("no File for main task found")  }
            else it.readText().replace("\r\n", "\n")
        }




    fun fetchInput(dir: String) : String {
        println("fetching input for $dir")
        val result = String(httpGet {
            url( "https://adventofcode.com/${dir.replace("day", "day/")}/input")
            header { cookie { "session" to System.getenv("AOC_COOKIE") } }
        }.body()!!.bytes()).trim()
        val target = File("./src/test/resources/$dir")
        target.mkdirs()
        val input = File("./src/test/resources/$dir/input.txt")
        if(input.exists())  input.delete()
        input.createNewFile()
        input.writeText(result)
        return result
    }
}