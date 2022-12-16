package de.twittgen.aoc.util

fun Class<*>.getIdentifier() = "${getYearFromPackage()}/${simpleName}"
fun Class<*>.getYearFromPackage() = packageName.split('.').last().drop(1)
