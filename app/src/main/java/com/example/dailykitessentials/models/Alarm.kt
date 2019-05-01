package com.example.dailykitessentials.models

import com.example.dailykitessentials.helpers.*
import java.util.*


class Alarm {
    // Properties set from database
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

    // Properties set by computation
    var ActiveTime : Date? = null
    var ElapsedTime : Long = 0

    var Challenge : Challenge? = null

    fun getDayOfWeek() : String {
        val sDateParts = SetTime.toString().split(String.MONOSPACE)
        return sDateParts[0]
    }

    fun computeElapsedTime() {
        ElapsedTime = (ActiveTime!!.time - Date().time) / 1000 // In seconds
    }

    fun getElapsedTime() : String {
        val ut = Utilities()
        val comps = ut.convertTimeDifferenceToTimeComponents(ElapsedTime)

        return (if (comps[0] == 0) "" else (comps[0].toString() + "h ")) +
                (if (comps[1] == 0) "left" else (comps[1].toString() + "m left"))
    }

    fun computeActiveTime(current : Date) {
        val ut = Utilities()

        var dateParts = current.toString().split(String.MONOSPACE)
        val timeParts = ut.getTime(SetTime)

        var sActiveTime = dateParts[0] + " " + dateParts[1] + " " + dateParts[2] + " " + timeParts + ":00 " + dateParts[4] + " " + dateParts[5]
        var activeTime = DATE_FORMATTER.parse(sActiveTime)

        if (activeTime.before(Date())) {
            val calendar = Calendar.getInstance()
            calendar.time = current
            calendar.add(Calendar.DAY_OF_YEAR, 1)

            dateParts = calendar.time.toString().split(String.MONOSPACE)

            sActiveTime = dateParts[0] + " " + dateParts[1] + " " + dateParts[2] + " " + timeParts + ":00 " + dateParts[4] + " " + dateParts[5]
            activeTime = DATE_FORMATTER.parse(sActiveTime)
        }

        ActiveTime = activeTime
    }
}