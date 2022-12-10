package de.twittgen.aoc.current.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.current.y2022.Day10.Instruction
import de.twittgen.aoc.util.second

class Day10: Day<List<Instruction>>() {
    override fun String.parse() = lines().map { when(it) {
        "noop" -> NOOP
        else -> ADD(it.split(" ").second().toInt())
    } }

    private val exampleExpected = """
##  ##  ##  ##  ##  ##  ##  ##  ##  ##  
###   ###   ###   ###   ###   ###   ### 
####    ####    ####    ####    ####    
#####     #####     #####     #####     
######      ######      ######      ####
#######       #######       #######     
 """
    init {
        part1(13140, 15140) { run().findSignificant().sumOf { it.first * it.second } }
        part2(exampleExpected) { "\n" + run().chunked(40).joinToString("\n") { it.draw() } }
    }

    private fun Signal.draw() = joinToString("") { (i, x) -> if ((pixel(i)) in sprite(x)) "#" else " " }

    private fun sprite(x: Int) = x - 1..x + 1
    private fun pixel(i: Int) = (i - 1) % 40

    private tailrec fun List<Instruction>.run(signal: Signal  = listOf(1 to 1)): Signal {
        return if (isEmpty())  signal else drop(1).run(first()(signal))
    }

    sealed class Instruction {
        abstract operator fun invoke(signal: Signal): Signal
    }
    object NOOP : Instruction() {
        override operator fun invoke(signal: Signal) = signal.next()
    }
    class ADD(private val amount: Int): Instruction() {
        override operator fun invoke(signal: Signal) = signal.next().next {it + amount}
    }

    private fun Signal.findSignificant() = filter { (i,_) -> (i-20) % 40  == 0  }

    override val example = """
        addx 15
        addx -11
        addx 6
        addx -3
        addx 5
        addx -1
        addx -8
        addx 13
        addx 4
        noop
        addx -1
        addx 5
        addx -1
        addx 5
        addx -1
        addx 5
        addx -1
        addx 5
        addx -1
        addx -35
        addx 1
        addx 24
        addx -19
        addx 1
        addx 16
        addx -11
        noop
        noop
        addx 21
        addx -15
        noop
        noop
        addx -3
        addx 9
        addx 1
        addx -3
        addx 8
        addx 1
        addx 5
        noop
        noop
        noop
        noop
        noop
        addx -36
        noop
        addx 1
        addx 7
        noop
        noop
        noop
        addx 2
        addx 6
        noop
        noop
        noop
        noop
        noop
        addx 1
        noop
        noop
        addx 7
        addx 1
        noop
        addx -13
        addx 13
        addx 7
        noop
        addx 1
        addx -33
        noop
        noop
        noop
        addx 2
        noop
        noop
        noop
        addx 8
        noop
        addx -1
        addx 2
        addx 1
        noop
        addx 17
        addx -9
        addx 1
        addx 1
        addx -3
        addx 11
        noop
        noop
        addx 1
        noop
        addx 1
        noop
        noop
        addx -13
        addx -19
        addx 1
        addx 3
        addx 26
        addx -30
        addx 12
        addx -1
        addx 3
        addx 1
        noop
        noop
        noop
        addx -9
        addx 18
        addx 1
        addx 2
        noop
        noop
        addx 9
        noop
        noop
        noop
        addx -1
        addx 2
        addx -37
        addx 1
        addx 3
        noop
        addx 15
        addx -21
        addx 22
        addx -6
        addx 1
        noop
        addx 2
        addx 1
        noop
        addx -10
        noop
        noop
        addx 20
        addx 1
        addx 2
        addx 2
        addx -6
        addx -11
        noop
        noop
        noop
    """.trimIndent()
}

 private typealias Signal = List<Pair<Int,Int>>
private fun Signal.next(transform: (Int) -> Int = {it}) =
    this + last().run { first + 1 to transform(second) }
