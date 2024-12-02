package de.twittgen.aoc.y2021

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.*

private const val EXPLODER_MARKER = "Ø"
private val splitOnExploder = Regex("(.*)$EXPLODER_MARKER(.*)")

class Day18: Day<List<SnailNumber>>(){
    override fun String.parse() = replace('[', '<')
            .replace(']', '>')
            .mapLines { it.toSnailNumber() }

    init {
        part1(4140, 4137) { it.reduce(SnailNumber::plus).getMagnitude() }
        part2(3993,4573) { it.flatMap { a -> it.map { b ->
                if(b!=a) (a+b).getMagnitude() else 0}
        }.maxOrNull()!! }
    }

    override val example = """
       [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
       [[[5,[2,8]],4],[5,[[9,9],0]]]
       [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
       [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
       [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
       [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
       [[[[5,4],[7,7]],8],[[8,3],8]]
       [[9,3],[[9,9],[6,[4,9]]]]
       [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
       [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
    """.trimIndent()
}

sealed class SnailNumber {
    operator fun plus(other: SnailNumber) = Nested(this, other).reduce()
    abstract fun copy(): SnailNumber
    abstract fun splitIfPossible(): Nested?
    abstract fun findExploder(currentDepth: Int = 0) : Nested?
    abstract fun getMagnitude() : Int
    abstract fun toExplodeString(exploder: Nested? = null): String
}

class Nested(val left: SnailNumber, val right: SnailNumber) : SnailNumber() {
    fun reduce(): SnailNumber = explodeIfPossible()?.reduce() ?: splitIfPossible()?.reduce() ?: this

    private fun explodeIfPossible()=  findExploder()?.let { explodeOn(it) }

    private fun explodeOn(exploder: Nested): Nested {
        val (front, back) = splitOnExploder.matchEntire(this.toExplodeString(exploder))!!.groupValues.drop(1)
        return listOf(
            explodeFront(front).let { (a, b, c) -> listOf(a, b.addSplash(exploder.left), c) },
            listOf("0"),
            explodeBack(back).let { (a, b, c) -> listOf(a, b.addSplash(exploder.right), c) }
        ).flatten().joinToString("").toSnailNumber() as Nested
    }

    private fun String.addSplash(exploder: SnailNumber) =
        if (isNotEmpty()) toInt() + (exploder as Terminal). value else this

    private fun explodeBack(s: String) = s.takePartitioning({ !it.isDigit() }, { it.isDigit() })
    private fun explodeFront(s: String) = s.takeLastPartitioning({ !it.isDigit() }, { it.isDigit() }).reversed()

    override fun splitIfPossible() =
        left.splitIfPossible()?.let { return Nested(it, right) } ?: right.splitIfPossible()?.let { Nested(left, it) }

    override fun getMagnitude() = (left.getMagnitude() * 3) + (right.getMagnitude() * 2)

    override fun findExploder(currentDepth: Int) =
        if(canExplode(currentDepth)) this else findChildExploder(currentDepth)

    private fun findChildExploder(currentDepth: Int) =
        left.findExploder(currentDepth + 1) ?: right.findExploder(currentDepth + 1)

    override fun copy() = Nested(left.copy(), right.copy())

    override fun toExplodeString(exploder: Nested?) =
        if (this == exploder) EXPLODER_MARKER else "<${left.toExplodeString(exploder)},${right.toExplodeString(exploder)}>"

    private fun canExplode(currentDepth: Int) = left is Terminal && right is Terminal && currentDepth >= 4
}

class Terminal(val value: Int): SnailNumber() {
    override fun copy() = Terminal(value)
    override fun splitIfPossible() =
        if(value > 9) (value / 2).let { Nested(Terminal(it), Terminal(value - it)) } else null
    override fun findExploder(currentDepth: Int): Nested?  = null
    override fun getMagnitude() =  value
    override fun toExplodeString( exploder: Nested?) = value.toString()
}

private fun String.toSnailNumber() = toNestedList().toSnailNumber()

private fun NestedList.toSnailNumber() : SnailNumber = when(this) {
       is NestedList.Terminal -> Terminal(value)
       is NestedList.Nested -> Nested(content.first().toSnailNumber(), content.second().toSnailNumber())
   }