package de.twittgen.aoc.util

data class Point3D(val x: Int, val y: Int, val z: Int) {
    companion object {
        val ORIGIN = Point3D(0,0,0)
    }
    operator fun minus(other: Point3D) = Point3D(x - other.x, y - other.y, z - other.z)
    fun getAdjacent() = listOf(
        Point3D(x-1, y, z),
        Point3D(x+1, y, z),
        Point3D(x, y-1, z),
        Point3D(x, y+1, z),
        Point3D(x, y, z-1),
        Point3D(x, y, z+1),
    )
}
