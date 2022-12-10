package de.twittgen.aoc.old.y2019.day6

class OrbitMap(private val map : Map<String,String>) {
    companion object {
        private const val ROOT_NODE = "COM"
        //Map is stored as Objects mapped to the Objects they orbit
        fun fromString(s: String): OrbitMap {
            return OrbitMap(
                s.filterNot{it == '\r'}
                    .split("\n")
                    .map {
                        it.substringAfter(")") to it.substringBefore(")")
                    }.toMap()
            )
        }
    }

    fun getIndirectOrbitCount()  = map.keys.map{ getPathToRoot(it).size }.sum()

    fun getPath(p1: String, p2: String): List<String> {
        val p1ToRoot = getPathToRoot(p1)
        val p2ToRoot = getPathToRoot(p2)
        return p1ToRoot.takeWhile { !p2ToRoot.contains(it) } + p1ToRoot.first { p2ToRoot.contains(it)} + p2ToRoot.takeWhile { !p1ToRoot.contains(it) }.reversed()
    }

    private tailrec fun getPathToRoot(s: String, currentPath: List<String> = emptyList()): List<String> {
        val parent = (map[s] ?: error("Node $s not found"))
        if (parent == ROOT_NODE) return currentPath.plus(parent)
        return getPathToRoot(parent,currentPath.plus(parent))
    }
}