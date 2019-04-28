package com.example.dailykitessentials.helpers

val String.Companion.EMPTY: String
    get() { return "" }

val String.Companion.MONOSPACE: String
    get() { return " " }

val MONTH_SET : Map<String, Int> = mapOf(
    "Jan" to 1,
    "Feb" to 2,
    "Mar" to 3,
    "Apr" to 4,
    "May" to 5,
    "Jun" to 6,
    "Jul" to 7,
    "Aug" to 8,
    "Sep" to 9,
    "Oct" to 10,
    "Nov" to 11,
    "Dec" to 12
)

class ModelFactory {

}