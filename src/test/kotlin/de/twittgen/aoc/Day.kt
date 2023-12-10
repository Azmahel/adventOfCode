package de.twittgen.aoc

import de.twittgen.aoc.Day.TestMarker.HACKY
import de.twittgen.aoc.Day.TestMarker.SLOW
import de.twittgen.aoc.Day.TestState.EXAMPLE
import de.twittgen.aoc.Day.TestState.REAL
import de.twittgen.aoc.util.FileUtil
import de.twittgen.aoc.util.getIdentifier
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assumptions.assumeTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayNameGeneration(DayNameGenerator::class)
abstract class  Day<R> {

    private var part1 : Part<R>? = null
    private var part2 : Part<R>? = null

    var mutableModel: Boolean = false
    var markersToSkip: Set<TestMarker> = setOf(SLOW)
    var markersToNote: Set<TestMarker> = setOf(HACKY)

    enum class TestMarker {
        SLOW , HACKY;
        open fun apply(d: Day<*>) {
            if(this in d.markersToSkip) d.skip("SKIPPED - marked as $this")
            if(this in d.markersToNote) println("solution marked as $this")
        }
    }

    abstract fun String.parse() : R

    fun part1(expectedExample: Any?, expected: Any? = null, vararg types: TestMarker, function: (R) -> Any?) {
        part1 = Part(function, expectedExample, expected, "PART1", types.toSet())
    }

    fun part2(expectedExample: Any?, expected: Any? = null, vararg types: TestMarker, function: (R) -> Any?) {
        part2 = Part(function, expectedExample, expected, "PART2", types.toSet())
    }

    open val example : String? = null

    private val exampleParsed by lazy { example!!.parse() }
    private val identifier = this.javaClass.getIdentifier().lowercase()
    protected var testState = EXAMPLE
        private set
    enum class TestState { EXAMPLE, REAL}

    private val rawInput by lazy {FileUtil.readInput(identifier)}
    private val input by lazy { rawInput.parse() }
    protected var raw : String? = null
        private set

    @BeforeAll
    fun init() { println("Running $identifier") }

    @Nested
    inner class Part1: RunPart(part1)

    @Nested
    inner class Part2: RunPart(part2)

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    open inner class RunPart(val part: Part<R>?) {
        @BeforeAll
        fun start() {
            if(part == null) skip("SKIPPED - does not exist")
            println("===${part!!.title}===")
        }

        @Test
        @DisplayName("Example")
        fun example() { part!!.runExample() }

        @Test
        @DisplayName("Real")
        fun real() { part!!.run() }

        private fun Part<R>.run() {
            assumeTrue(rawInput.isNotEmpty())
            markers.forEach { it.apply(this@Day) }
            testState = REAL
            raw = rawInput
            run(if (mutableModel) rawInput.parse() else input, "real")
        }

        private fun Part<R>.runExample() {
            if(exampleExpected == null)  skip("SKIPPED - marked as not applicable")
            testState =EXAMPLE
            raw = example
            run(if(mutableModel) example!!.parse() else exampleParsed, "example")
        }

        private fun Part<R>.run(input: R, title: String) {
            val result = function(input).also { println("$title: $it") }
            (if (testState == EXAMPLE) exampleExpected else this.expected)?.let {
                assertEquals(it.toString(), result.toString())
            } ?: println("NO ASSERTION").also { fail("") }
        }
    }

    private fun skip(s: String? = null) =  s?.let { println(s) }.also { assumeTrue(false) }

    data class Part<R>(val function: (R) -> Any?, val exampleExpected: Any?, val expected: Any? = null, val title: String, val markers: Set<TestMarker> = emptySet())
}


