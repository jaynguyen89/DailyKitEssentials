package com.example.dailykitessentials.models

import java.util.*
import com.example.dailykitessentials.helpers.EMPTY
import com.example.dailykitessentials.helpers.MONOSPACE
import com.example.dailykitessentials.helpers.MONTH_SET
import java.time.DayOfWeek


class Alarm {

    var Id : Int = 0
    var AlarmType : Int = 0
    var IsTemporary : Boolean = false
    var SetTime : Date = Date()
    var RepeatDays : String = String.EMPTY //arrayListOf<Int>()
    var SoundName : String = String.EMPTY
    var VolumeLevel : Int = 10
    var VibrationPattern : String = String.EMPTY
    var SnoozeDuration : Int = 5
    var AlarmNote : String = String.EMPTY
    var IsActive : Boolean = false

    var Challenge : Challenge? = null

    fun getDate() : String {
        val sDateParts = SetTime.toString().split(String.MONOSPACE)
        val sMonth = if(MONTH_SET[sDateParts[1]]!! < 10) "0" + MONTH_SET[sDateParts[1]] else MONTH_SET[sDateParts[1]].toString()

        return sDateParts[2] + "-" + sMonth + "-" + sDateParts[5]
    }

    fun getTime() : String {
        val sDateParts = SetTime.toString().split(String.MONOSPACE)
        val sTimeParts = sDateParts[3].split(":")

        return (if (sTimeParts[0].length == 1) "0" + sTimeParts[0] else sTimeParts[0]) + ":" + sTimeParts[1]
    }

    fun getDayOfWeek() : String {
        val sDateParts = SetTime.toString().split(String.MONOSPACE)
        return sDateParts[0]
    }
}