package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.y2022.Day07.Directory
import de.twittgen.aoc.util.second
import de.twittgen.aoc.util.secondOrNull
import java.lang.IllegalStateException


class Day07 : Day<Directory>(){
    override fun String.parse(): Directory = Directory().also { it.performNextInstruction(lines().drop(1)) }

    init {
        mutableModel = true
        part1(95437, 1886043) {
            it.getSubdirectoriesIncludingSelf().filter { d -> d.size() <= 100_000 }.sumOf { it.size() }
        }
        part2(24933642, 3842121) { it.getSubdirectoriesIncludingSelf()
            .map { d -> d.size() }.filter { s -> s > -40000000 + it.size() }.minOrNull()!!
        }
    }

    private tailrec fun Directory.performNextInstruction(instructions: List<String>) {
        if (instructions.isEmpty()) return
        assert(instructions.first().startsWith('$'))
        val (cmd, param) = (instructions.first().drop(2).split(" ")).run { first() to secondOrNull() }
        val remainder = instructions.drop(1)
        when(cmd) {
            "cd" -> { when(param) {
                ".." -> parent!!.performNextInstruction(remainder)
                else -> (children[param] as Directory).performNextInstruction(remainder)
            }}
            "ls" -> {
                remainder.takeWhile { !it.startsWith('$') }.forEach {
                    val (obj, name) = it.split(" ").run { first() to second() }
                    children += when(obj) {
                        "dir" -> name to Directory(this)
                        else -> name to File(obj.toLong())
                    }
                }
                this.performNextInstruction(remainder.dropWhile { !it.startsWith('$') })
            }
            else -> throw IllegalStateException()
        }
    }

    sealed class FSObject { abstract fun size(): Long }
    data class File(val size: Long) : FSObject() { override fun size() = size }
    class Directory(val parent: Directory? = null, var children : Map<String, FSObject> = emptyMap() ) : FSObject() {
        override fun size() = children.map { it.value.size() }.sum()
        private fun getSubdirectories(): List<Directory> = children.values
            .filterIsInstance<Directory>()
            .flatMap {  it.getSubdirectories() + it }
        fun getSubdirectoriesIncludingSelf() = getSubdirectories() + this
    }

    override val example = """
        ${'$'} cd /
        ${'$'} ls
        dir a
        14848514 b.txt
        8504156 c.dat
        dir d
        ${'$'} cd a
        ${'$'} ls
        dir e
        29116 f
        2557 g
        62596 h.lst
        ${'$'} cd e
        ${'$'} ls
        584 i
        ${'$'} cd ..
        ${'$'} cd ..
        ${'$'} cd d
        ${'$'} ls
        4060174 j
        8033020 d.log
        5626152 d.ext
        7214296 k
    """.trimIndent()
}