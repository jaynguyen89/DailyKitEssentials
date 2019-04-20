package com.example.dailykitessentials.models

import java.util.*
import com.example.dailykitessentials.helpers.EMPTY


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
    var Challenge : Challenge? = null
}