package de.twittgen.aoc

import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.lang.IllegalStateException

abstract class  Day<T,V, R>(
     contextBuilder: Context<T,V>.()->Unit
     ) {
     class Context<T,V> {
        var part1: Part<T>? = null
        var part2: Part<V>? = null
        lateinit var example :String
        fun part1(builder: Part<T>.()->Unit) {
            part1 = Part<T>().apply(builder)
        }
        fun part2(builder: Part<V>.()->Unit) {
            part2 = Part<V>().apply(builder)
        }
     }

     class Part<T> {
        var exampleExpected : T? = null;
        var expected : T? = null;
    }

     private val context: Context<T,V>

     init {
         context = Context<T,V>().apply(contextBuilder)
     }

     abstract fun parseInput(s: String) : R
     open fun part1(input: R) : T {
         println("Part1 not yet implemented")
         throw IllegalStateException()
     }
    open fun part2(input: R) : V {
        println("Part2 not yet implemented")
        throw IllegalStateException()
    }

     val example = parseInput(context.example)
     val input = parseInput(FileUtil.readInput("2022/${this.javaClass.simpleName.lowercase()}"))

    @Test
    fun run() {
        context.part1?.also {
            println("PART1")
            runPart(it, this::part1)
        }
        context.part2?.also {
            println("PART2")
            runPart(it, this::part2)
        }

    }

    fun <K>runPart(part: Part<K>, partFunction: (R) -> K) {
        val exampleResult = partFunction(example)
        println("example: $exampleResult")
        part.exampleExpected?.also { expected ->
            assertEquals(expected, exampleResult)
        }

        val result = partFunction(input)
        println("real: $result")
        part.expected?.also { expected ->
            assertEquals(expected, result)
        }
    }
}



