package de.twittgen.aoc.current

import de.twittgen.aoc.current.Day.TestState.EXAMPLE
import de.twittgen.aoc.current.Day.TestState.REAL
import de.twittgen.aoc.current.Day.TestType.NORMAL
import de.twittgen.aoc.current.Day.TestType.SLOW
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

    protected var mutableModel: Boolean = false
    protected var runSlowTests: Boolean = false
    enum class TestType { SLOW, NORMAL}
    private var slowTests = emptyList<Part<R>>()

    abstract fun String.parse() : R

    fun part1(expectedExample: Any?, expected: Any? = null, type: TestType = NORMAL, function: (R) -> Any?) {
        part1 = Part(function, expectedExample, expected, "PART1")
        if(type == SLOW) slowTests = slowTests + part1!!
    }

    fun part2(expectedExample: Any?, expected: Any? = null, type: TestType = NORMAL, function: (R) -> Any?) {
        part2 = Part(function, expectedExample, expected, "PART2")
        if(type == SLOW) slowTests = slowTests + part2!!
    }

    open val example : String? = null

    private val exampleParsed by lazy { example!!.parse() }
    private val identifier = this.javaClass.getIdentifier().lowercase()
    protected var testState = EXAMPLE
        private set
    enum class TestState { EXAMPLE, REAL}

    private val rawInput by lazy {FileUtil.readInput(identifier)}
    private val input by lazy { rawInput.parse() }

    @BeforeAll
    fun init() {
        println("Running $identifier")

    }

    @Test
    fun part1() {
        assumeTrue(part1 !=null, "PART 1 skipped: does not exist")
        part1!!.run()
    }

    @Test
    fun part2() {
        assumeTrue(part2 !=null, "PART2 skipped: does not exist")
        part2!!.run()
    }

    private fun Part<R>.run() {
        println("===$title===")
        if (exampleExpected != null) {
            testState =EXAMPLE
            run(
            function,
            if(mutableModel) example!!.parse() else exampleParsed,
            exampleExpected,
            "example"
        )}
        if(rawInput.isNotEmpty()) {
            if(!runSlowTests && this in slowTests) {
                println("skipped real: marked as slow").also { assumeTrue(false)}
            }
            testState = REAL
            run(
                function,
                if (mutableModel) rawInput.parse() else input,
                expected,
                "real")
        }
    }

    private fun run(partFunction: (R) -> Any?, input: R, expected: Any?, title: String) {
        val result = partFunction(input).also { println("$title: $it") }
        expected?.also { assertEquals(it.toString(), result.toString()) }
    }

    data class Part<R>(
        var function: (R) -> Any?,
        var exampleExpected: Any?,
        var expected: Any? = null,
        val title: String
    )


}


