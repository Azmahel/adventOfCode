package de.twittgen.aoc.y2022

import de.twittgen.aoc.Day
import de.twittgen.aoc.util.second
import de.twittgen.aoc.util.secondOrNull
import java.lang.IllegalStateException


class Day7 : Day<Long, Long, Day7.Directory>(){

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

    sealed class FSObject { abstract fun size(): Long }
    data class File(val size: Long) : FSObject() { override fun size() = size }
    class Directory(val parent: Directory? = null, var children : Map<String, FSObject> = emptyMap() ) : FSObject() {
        override fun size() = children.map { it.value.size() }.sum()
        private fun getSubdirectories(): List<Directory> = children.values
            .filterIsInstance<Directory>()
            .flatMap {  it.getSubdirectories() + it }
        fun getSubdirectoriesIncludingSelf() = getSubdirectories() + this
    }

    override fun String.parse(): Directory = Directory().also { performNextInstruction(it, lines().drop(1)) }

    init {
        part1(95437, 1886043) {
            getSubdirectoriesIncludingSelf().filter { it.size() <= 100_000 }.sumOf { it.size() }
        }

        part2(24933642) {
            getSubdirectoriesIncludingSelf().map { it.size() }.filter { it > -40000000 + size() }.minOrNull()!!
        }
    }

    private fun performNextInstruction(currentDirectory : Directory, instructions : List<String>) {
        if (instructions.isEmpty()) return
        assert(instructions.first().startsWith('$'))
        val (cmd, param) =  (instructions.first().drop(2).split(" ")).run {
            first() to secondOrNull()
        }
        val remainder = instructions.drop(1)
        when(cmd) {
            "cd" -> {
                when(param) {
                    ".." -> performNextInstruction(currentDirectory.parent!! , remainder)
                    else -> performNextInstruction(currentDirectory.children[param] as Directory, remainder)
                }
            }
            "ls" -> {
                remainder.takeWhile { !it.startsWith('$') }.forEach {
                    val (obj, name) = it.split(" ").run { first() to second() }
                    when(obj) {
                        "dir" -> currentDirectory.children += name to Directory(currentDirectory)
                        else ->  currentDirectory.children += name to File( obj.toLong())
                    }
                }
                performNextInstruction(currentDirectory, remainder.dropWhile { !it.startsWith('$') })
            }
            else -> throw IllegalStateException()
        }
    }
}