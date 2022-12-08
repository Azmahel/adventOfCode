package de.twittgen.aoc.y2019.day2
private const val ADD = 1
private const val MULT = 2
private const val STOP = 99
class IntCodeComputer(private val program: List<Int>) {

    fun run(noun: Int, verb: Int): Int = run(program.take(1) + noun +verb + program.drop(3)).first()

    private fun run(input: List<Int>): List<Int> {
        val r =input.toMutableList()
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