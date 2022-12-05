package de.twittgen.aoc

import de.twittgen.aoc.y2019.shared.util.FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.lang.IllegalStateException

abstract class  Day<T, V, R> {

    private var part1 : Part<T, R>? = null
    private var part2 : Part<V, R>? = null

    protected var mutableModel: Boolean = false

    abstract fun String.parse() : R


    fun part1(expectedExample : T, expected : T? = null, function : R.() -> T ) { part1 = Part(function, expectedExample, expected) }
    fun part2(expectedExample : V, expected : V? = null, function : R.() -> V ) { part2 = Part(function, expectedExample, expected) }

    abstract val example : String
    private val exampleParsed by lazy { example.parse() }
    private val rawInput by lazy {FileUtil.readInput("2022/${this.javaClass.simpleName.lowercase()}")}
    private val input by lazy { rawInput.parse() }

    @Test
    fun run() {
        part1?.also { part -> println("PART1").also { runPart(part) } }
        part2?.also { part ->  println("PART2").also { runPart(part) } }
    }

    private fun <K>runPart(part: Part<K,R>, ) {
        run(
            part.function,
            if(mutableModel) example.parse() else exampleParsed,
            part.exampleExpected,
            "example"
        )
        if(rawInput.isNotEmpty()) {
            run(
                part.function,
                if (mutableModel) rawInput.parse() else input,
                part.expected,
                "real")
        }
    }

    private fun <K> run(partFunction: (R) -> K, input: R, expected: K?, title: String) {
        val result = partFunction(input).also { println("$title: $it") }
        expected?.also { assertEquals(it, result) }
    }


    data class Part<T,R> (
        var function : R.() -> T,
        var exampleExpected : T,
        var expected : T? = null,
    )
}



