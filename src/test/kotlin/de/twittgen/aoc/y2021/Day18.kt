package de.twittgen.aoc.y2021

import de.twittgen.aoc.util.FileUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.collections.ArrayDeque

class Day18 {
    val input by lazy { FileUtil.readInput("2021/day18").parse() }
    val example = """[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
[[[5,[2,8]],4],[5,[[9,9],0]]]
[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
[[[[5,4],[7,7]],8],[[8,3],8]]
[[9,3],[[9,9],[6,[4,9]]]]
[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]""".parse()

    private fun String.parse() = lines().map { it.toSnailNumber() }

    sealed class SnailNumber
    class Nested(val left: SnailNumber, val right: SnailNumber) : SnailNumber()
    class Terminal(val value: Int) : SnailNumber()

    private fun String.toSnailNumber(): SnailNumber {
        val placeholders = ArrayDeque(('a'..'z').toList() + ('A'..'Z').toList())
        var remaining = this.replace('[', '<').replace(']', '>')
        val mapping = mutableMapOf<Char, SnailNumber>()
        while (remaining.length > 1) {
            Regex(".*(<[^<>]*>).*")
                .matchEntire(remaining)?.groupValues?.drop(1)?.forEach {
                    val (a, b) = it.drop(1).dropLast(1).split(",")
                    val p = placeholders.removeFirst()
                    mapping[p] = getNested(a, b, mapping)
                    remaining = remaining.replace(Regex(it), p.toString())
                }
        }
        return mapping[remaining.first()]!!
    }

    private fun getNested(
        a: String,
        b: String,
        mapping: MutableMap<Char, SnailNumber>
    ) = Nested(
        if (a.isTerminal()) Terminal(a.toInt()) else mapping[a.first()]!!.copy(),
        if (b.isTerminal()) Terminal(b.toInt()) else mapping[b.first()]!!.copy()
    )

    private fun SnailNumber.copy(): SnailNumber {
        return when(this) {
            is Terminal -> Terminal(value)
            is Nested -> Nested(left.copy(), right.copy())
        }
    }

    private fun String.isTerminal() = toIntOrNull() != null

    private infix operator fun SnailNumber.plus(other: SnailNumber) = Nested(this, other).reduce()

    private fun Nested.reduce(): SnailNumber {
        var changed = true
        var current = this as SnailNumber
        while(changed){
            val (newExp, exploded) = current.explodeFirst()
            current = newExp
            changed = exploded
            if(!changed) {
                val (new, split) = current.splitFirst()
                changed = split
                current = new
            }
        }
        return current
    }
    private fun SnailNumber.explodeFirst(): Pair<SnailNumber, Boolean> {
        val explodee = findExplodee()  ?: return this to false
        val expL = (explodee.left as Terminal).value
        val expR = (explodee.right as Terminal).value
        val explodeSequence = toExplodeSequence(explodee)

        val front = explodeSequence.takeWhile { it !="X" }
        var prefix = front.dropLastWhile { it.toIntOrNull() == null }
        val left = prefix.lastOrNull()?.toInt()
        val leftCenter = front.takeLastWhile { it.toIntOrNull() == null }
        prefix = prefix.dropLast(1)

        val back = explodeSequence.takeLastWhile { it != "X" }
        var suffix = back.dropWhile { it.toIntOrNull() == null }
        val right = suffix.firstOrNull()?.toInt()
        val rightCenter = back.takeWhile { it.toIntOrNull() == null }
        suffix = suffix.drop(1)
        val new = prefix +
                listOfNotNull(left?.plus(expL)?.toString()) +
                leftCenter +
                "0" +
                rightCenter +
                listOfNotNull(right?.plus(expR)?.toString()) +
                suffix

        return new.joinToString("").toSnailNumber() to true
    }

    private fun SnailNumber.toExplodeSequence(explodee: Nested? = null): List<String> {
        if(this === explodee) return listOf("X")
        return when(this) {
            is Terminal ->  listOf(value.toString())
            is Nested -> {
                 listOf("[") +
                         left.toExplodeSequence(explodee) +
                         "," +
                         right.toExplodeSequence(explodee) +
                         listOf("]")
            }
        }
    }


    private fun SnailNumber.findExplodee(currentDepth: Int = 0) : Nested? {
        return when(this) {
            is Terminal -> null
            is Nested -> {
                if(left is Terminal && right is Terminal && currentDepth >= 4 ) {
                    this
                } else {
                    left.findExplodee(currentDepth +1) ?: right.findExplodee(currentDepth + 1)
                }
            }
        }
    }

    private fun SnailNumber.splitFirst(): Pair<SnailNumber, Boolean> {
        return when(this) {
            is Terminal ->{
                if(value > 9) {
                    val halved = (value / 2)
                    Nested(Terminal(halved), Terminal(value - halved)) to true
                } else {
                    this to false
                }
            }
            is Nested -> {
                val (newL, splitL) = left.splitFirst()
                if(splitL) {
                    Nested(newL, right) to true
                } else {
                    val (newR, splitR) = right.splitFirst()
                    Nested(left, newR) to splitR
                }
            }
        }
    }

    private fun SnailNumber.getMagnitude() : Int {
        return when(this) {
            is Terminal -> value
            is Nested -> (left.getMagnitude() * 3) + (right.getMagnitude()*2)
        }
    }

    @Test
    fun example() {
        val result = example.reduce { a, b -> a+b}.getMagnitude()
        assertEquals(4140, result)
    }

    @Test
    fun example2() {
        val result = example
            .flatMap { a ->
                example.map { b -> if(b!=a) (a+b).getMagnitude() else 0}
            }.maxOrNull()
        assertEquals(3993, result)
    }

    @Test
    fun part1() {
        val result = input.reduce { a, b -> a+b}.getMagnitude()
        println(result)
    }

    @Test
    fun part2() {
        val result = input
            .flatMap { a ->
                input.map { b -> if(b!=a) (a+b).getMagnitude() else 0}
            }.maxOrNull()
        println(result)
    }
}



