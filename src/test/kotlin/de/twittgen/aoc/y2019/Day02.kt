package de.twittgen.aoc.y2019

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.IntCodeProgram
import de.twittgen.aoc.util.toIntCodeProgram

class Day02 : Day<IntCodeProgram>(){
    override fun String.parse() = toIntCodeProgram()

    init {
        part1(null, 3765464) { IntCodeComputer(it).run(12, 2)}
        part2(null, 7610) { IntCodeComputer(it).runSampling() }
    }

    private fun IntCodeComputer.runSampling(target: Int = 19690720 ) : Int {
        (0..99).forEach { noun -> (0..99).forEach { verb ->
                if (run(noun, verb) == target) return  100 * noun + verb
        } }
        throw IllegalStateException()
    }

    class IntCodeComputer(private val program: List<Int>) {
        fun run(noun: Int, verb: Int): Int = run(program.take(1) + noun + verb + program.drop(3)).first()
        private fun run(input: List<Int>): List<Int> {
            val r = input.toMutableList()
            var i = 0
            while (i <= r.size) {
                when (r[i]) {
                    ADD -> r[r[i + 3]] = r[r[i + 1]] + r[r[i + 2]]
                    MULT -> r[r[i + 3]] = r[r[i + 1]] * r[r[i + 2]]
                    STOP -> return r
                    else -> throw IllegalArgumentException("error in Program at position $i")
                }
                i += 4
            }
            return r
        }
    }
}
private const val ADD = 1
private const val MULT = 2
private const val STOP = 99