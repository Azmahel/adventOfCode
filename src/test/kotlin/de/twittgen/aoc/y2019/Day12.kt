package de.twittgen.aoc.y2019

import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue
import kotlin.math.sign

class Day12 {
    val moons = listOf(
        Moon(
            Coordinate(
                6,
                10,
                10
            )
        ),
        Moon(
            Coordinate(
                -9,
                3,
                17
            )
        ),
        Moon(
            Coordinate(
                9,
                -4,
                14
            )
        ),
        Moon(
            Coordinate(
                4,
                14,
                4
            )
        )
    )
    @Test
    fun getA() {

        repeat(1000) {
            doStep(moons)
        }
        val x = moons.map {
            it.getEnergy()
        }.sum()
        println(x)
    }

    @Test
    fun getB() {
        var foundX = false; var foundY = false; var foundZ = false
        var  x = 1L; var y=1L; var z =1L
        doStep(moons)
        while(!(foundX && foundY && foundZ)) {
            doStep(moons)
            if(!foundX) x++
            if(!foundY) y++
            if(!foundZ) z++
            foundX = foundX  || moons.all{it.velocity.x == 0}
            foundY = foundY  || moons.all{it.velocity.y == 0}
            foundZ = foundZ  || moons.all{it.velocity.z == 0}
        }
        val xy = lcm(x,y)
        val xyz = lcm(xy,z)
        println(xyz*2)
    }

    fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
    fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b

    @Test
    fun getB2() {
        val xs = listOf(
            PV(6),
            PV(-9),
            PV(9),
            PV(4)
        )
         doStepSingleAxis(xs)
        var x = 1
        while(xs.firstOrNull{it.v !=0} != null) {
            doStepSingleAxis(xs)
            x ++
        }
        println(x)
    }

    fun doStepSingleAxis(on : List<PV>) =
        on.forEach{ it ->
            on.forEach { other ->
                it.v += (other.p - it.p).sign
            }
            it.p += it.v
        }


    fun doStep(moons: List<Moon>) {
        moons.forEach {
            it.updateVelocity(moons)
        }
        moons.forEach {
            it.move()
        }
    }
    data class PV( var p : Int, var v: Int =0)

    fun Moon.updateVelocity(moons: List<Moon>) {
        moons.forEach {
            this.velocity.x +=  (it.position.x - this.position.x).sign
            this.velocity.y +=  (it.position.y - this.position.y).sign
            this.velocity.z +=  (it.position.z - this.position.z).sign
        }
    }
    data class Moon(
        val position: Coordinate,
        val velocity: Coordinate = Coordinate(
            0,
            0,
            0
        )
    ) {
        fun move() {
            position.x += velocity.x
            position.y += velocity.y
            position.z += velocity.z
        }

        fun getEnergy(): Int = (
                position.run {
                    x.absoluteValue + y.absoluteValue + z.absoluteValue
                }) * (
                velocity.run {
                    x.absoluteValue + y.absoluteValue + z.absoluteValue
                }
                )
    }

    data class Coordinate(
        var x : Int,
        var y : Int,
        var z: Int
        )
}