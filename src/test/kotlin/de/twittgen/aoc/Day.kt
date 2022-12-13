package de.twittgen.aoc

import de.twittgen.aoc.util.FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class  Day<R> {

    private var part1 : Part<R>? = null
    private var part2 : Part<R>? = null

    protected var mutableModel: Boolean = false

    abstract fun String.parse() : R

    fun part1(expectedExample: Any?, expected: Any? = null, function: (R) -> Any) {
        part1 = Part(function, expectedExample, expected)
    }

    fun part2(expectedExample: Any?, expected: Any? = null, function: (R) -> Any) {
        part2 = Part(function, expectedExample, expected)
    }

    open val example : String? = null
    private val exampleParsed by lazy { example!!.parse() }
    private val identifier = "${getYearFormPackage()}/${this.javaClass.simpleName.lowercase()}"

    private val rawInput by lazy {FileUtil.readInput(identifier)}
    private val input by lazy { rawInput.parse() }

    @BeforeAll
    fun init() {
        println("Running $identifier")
    }

    @Test
    fun part1() {
        assumeTrue(part1 !=null)
        part1!!.also { part -> println("PART1").also { part.run() } }
    }

    @Test
    fun part2() {
        assumeTrue(part2 !=null)
        part2!!.also { part ->  println("PART2").also { part.run() } }
    }

    private fun Part<R>.run() {
        if (exampleExpected != null) {run(
            function,
            if(mutableModel) example!!.parse() else exampleParsed,
            exampleExpected,
            "example"
        )}
        if(rawInput.isNotEmpty()) {
            run(
                function,
                if (mutableModel) rawInput.parse() else input,
                expected,
                "real")
        }
    }

    private fun run(partFunction: (R) -> Any, input: R, expected: Any?, title: String) {
        val result = partFunction(input).also { println("$title: $it") }
        expected?.also { assertEquals(it.toString(), result.toString()) }
    }

    data class Part<R>(
        var function: (R) -> Any,
        var exampleExpected: Any?,
        var expected: Any? = null,
    )

    private fun getYearFormPackage() = this.javaClass.packageName.split('.').last().drop(1)
}



