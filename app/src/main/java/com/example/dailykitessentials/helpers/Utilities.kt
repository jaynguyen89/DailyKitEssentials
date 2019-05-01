package com.example.dailykitessentials.helpers

import android.annotation.SuppressLint
import com.example.dailykitessentials.models.Alarm
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

val String.Companion.EMPTY: String
    get() { return "" }

val String.Companion.MONOSPACE: String
    get() { return " " }

val MONTHS_NAMES : Map<Int, String> = mapOf(
    1 to "Jan",
    2 to "Feb",
    3 to "Mar",
    4 to "Apr",
    5 to "May",
    6 to "Jun",
    7 to "Jul",
    8 to "Aug",
    9 to "Sep",
    10 to "Oct",
    11 to "Nov",
    12 to "Dec"
)

val MONTH_NUMS : Map<String, Int> = mapOf(
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

val MONTH_TEXTS : Map<String, String> = mapOf(
    "01" to "Jan", "Jan" to "01",
    "02" to "Feb", "Feb" to "02",
    "03" to "Mar", "Mar" to "03",
    "04" to "Apr", "Apr" to "04",
    "05" to "May", "May" to "05",
    "06" to "Jun", "Jun" to "06",
    "07" to "Jul", "Jul" to "07",
    "08" to "Aug", "Aug" to "08",
    "09" to "Sep", "Sep" to "09",
    "10" to "Oct", "Oct" to "10",
    "11" to "Nov", "Nov" to "11",
    "12" to "Dec", "Dec" to "12"
)

val NEXT_DAYS : Map<String, String> = mapOf(
    "Mon" to "Tue",
    "Tue" to "Wed",
    "Wed" to "Thu",
    "Thu" to "Fri",
    "Fri" to "Sat",
    "Sat" to "Sun",
    "Sun" to "Mon"
)

// For 24h format: dd/MM/yyyy HH:mm ---- For 12h format: dd/MM/yyyy hh:mm:ss a
@SuppressLint("SimpleDateFormat")
val SHORT_DATE_FORMATTER = SimpleDateFormat("dd/MM/yyyy HH:mm")
@SuppressLint("SimpleDateFormat")
val DATE_FORMATTER = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")

class Utilities {
    fun getDate(time : Date) : String {
        val sDateParts = time.toString().split(String.MONOSPACE)
        return sDateParts[2] + "-" + MONTH_TEXTS[sDateParts[1]] + "-" + sDateParts[5]
    }

    fun getTime(time : Date) : String {
        val sDateParts = time.toString().split(String.MONOSPACE)
        val sTimeParts = sDateParts[3].split(":")

        return sTimeParts[0] + ":" + sTimeParts[1]
    }

    fun convertTimeDifferenceToTimeComponents(difference : Long) : Array<Int> {
        val hours = Math.floor(difference / 3600.0).toInt()
        val minutes = Math.floor((difference - hours * 3600) / 60.0).toInt()

        return arrayOf(hours, minutes)
    }

    fun reorderAlarms(alarms : ArrayList<Alarm>) : ArrayList<Alarm> {
        TODO()
    }
}