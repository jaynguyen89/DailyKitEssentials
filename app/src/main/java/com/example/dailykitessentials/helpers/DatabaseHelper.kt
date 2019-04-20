package com.example.dailykitessentials.helpers

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf
import com.example.dailykitessentials.models.Alarm
import com.example.dailykitessentials.models.Challenge
import java.text.SimpleDateFormat

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "DailyKit.db"
        var DATABASE_VERSION = 1
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)

        // Enable foreign key on database (supported since API 16)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.run {
            execSQL(DatabaseSchema.Alarms.CREATE_TABLE_STATEMENT)
            execSQL(DatabaseSchema.Challenges.CREATE_TABLE_STATEMENT)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseSchema.Alarms.TABLE_NAME + ";")
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseSchema.Challenges.TABLE_NAME + ";")
        onCreate(db)
    }

    fun resetDatabase() {
        this.writableDatabase.apply {
            onUpgrade(this, DATABASE_VERSION, DATABASE_VERSION + 1)
            close()
        }
    }

    fun addNewAlarm(alarm : Alarm) : Long {
        val newAlarm = contentValuesOf()

        newAlarm.put(DatabaseSchema.Alarms.ALARM_TYPE, alarm.AlarmType)
        newAlarm.put(DatabaseSchema.Alarms.IS_TEMPORARY, if (alarm.IsTemporary) 1 else 0)
        newAlarm.put(DatabaseSchema.Alarms.SET_TIME, alarm.SetTime.toString())
        newAlarm.put(DatabaseSchema.Alarms.REPEAT_DAYS, alarm.RepeatDays)
        newAlarm.put(DatabaseSchema.Alarms.SOUND_NAME, alarm.SoundName)
        newAlarm.put(DatabaseSchema.Alarms.VOLUME_LEVEL, alarm.VolumeLevel)
        newAlarm.put(DatabaseSchema.Alarms.VIBRATION_PATTERN, alarm.VibrationPattern)
        newAlarm.put(DatabaseSchema.Alarms.SNOOZE_DURATION, alarm.SnoozeDuration)
        newAlarm.put(DatabaseSchema.Alarms.ALARM_NOTE, alarm.AlarmNote)
        newAlarm.put(DatabaseSchema.Alarms.CHALLENGE_ID, if (alarm.Challenge == null) null else addNewChallenge(alarm.Challenge!!))

        this.writableDatabase.run {
            return insert(DatabaseSchema.Alarms.TABLE_NAME, null, newAlarm)
        }
    }

    private fun addNewChallenge(challenge : Challenge) : Long {
        var newChallenge = contentValuesOf()

        newChallenge.put(DatabaseSchema.Challenges.CHALLENGE_TYPE, challenge.ChallengeType)
        newChallenge.put(DatabaseSchema.Challenges.CHALLENGE_TOUGHNESS, challenge.ChallengeToughness)

        when (challenge.ChallengeType) {
            0 -> newChallenge.put(DatabaseSchema.Challenges.PUZZLE_NUMBER, challenge.PuzzleNumber)
            1 -> newChallenge.put(DatabaseSchema.Challenges.SHOUT_NUMBER, challenge.ShoutNumber)
            2 -> newChallenge.put(DatabaseSchema.Challenges.SHAKE_NUMBER, challenge.ShakeNumber)
            3 -> newChallenge.put(DatabaseSchema.Challenges.BARCODE_NAME, challenge.BarcodeName)
            else -> newChallenge.put(DatabaseSchema.Challenges.KEY_CODE, challenge.KeyCodes)
        }

        this.writableDatabase.run {
            return insert(DatabaseSchema.Challenges.TABLE_NAME, null, newChallenge)
        }
    }

    fun getAllAlarms() : ArrayList<Alarm> {
        val allAlarms = arrayListOf<Alarm>()
        var c : Cursor

        this.readableDatabase.run {
            c = rawQuery("SELECT * FROM " + DatabaseSchema.Alarms.TABLE_NAME + ";", null)
        }

        if (c != null)
            while (c.moveToNext()) {
                val alarm = Alarm()

                alarm.Id = c.getInt(c.getColumnIndex(DatabaseSchema.Alarms.ID))
                alarm.AlarmType = c.getInt(c.getColumnIndex(DatabaseSchema.Alarms.ALARM_TYPE))
                alarm.IsTemporary = c.getInt(c.getColumnIndex(DatabaseSchema.Alarms.IS_TEMPORARY)) == 1

                // For 24h format: dd/MM/yyyy HH:mm ---- For 12h format: dd/MM/yyyy hh:mm:ss a
                val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
                alarm.SetTime = dateFormatter.parse(c.getString(c.getColumnIndex(DatabaseSchema.Alarms.SET_TIME)))

                alarm.RepeatDays = c.getString(c.getColumnIndex(DatabaseSchema.Alarms.REPEAT_DAYS))
                alarm.SoundName = c.getString(c.getColumnIndex(DatabaseSchema.Alarms.SOUND_NAME))
                alarm.VolumeLevel = c.getInt(c.getColumnIndex(DatabaseSchema.Alarms.VOLUME_LEVEL))
                alarm.VibrationPattern = c.getString(c.getColumnIndex(DatabaseSchema.Alarms.VIBRATION_PATTERN))
                alarm.SnoozeDuration = c.getInt(c.getColumnIndex(DatabaseSchema.Alarms.SNOOZE_DURATION))
                alarm.AlarmNote = c.getString(c.getColumnIndex(DatabaseSchema.Alarms.ALARM_NOTE))

                alarm.Challenge = getChallengeById(c.getInt(c.getColumnIndex(DatabaseSchema.Alarms.CHALLENGE_ID)))

                allAlarms.add(alarm)
            }

        c.close()

        return allAlarms
    }

    private fun getChallengeById(ChallengeId : Int) : Challenge {
        val Challenge = Challenge()
        Challenge.Id = ChallengeId

        val c = this.readableDatabase.query(
            DatabaseSchema.Challenges.TABLE_NAME,
            null,
            DatabaseSchema.Challenges.ID + " = ?",
            arrayOf(ChallengeId.toString()),
            null,
            null,
            null
        )

        if (c.moveToFirst()) {
            Challenge.ChallengeType = c.getInt(c.getColumnIndex(DatabaseSchema.Challenges.CHALLENGE_TYPE))
            Challenge.ChallengeToughness = c.getInt(c.getColumnIndex(DatabaseSchema.Challenges.CHALLENGE_TOUGHNESS))
            Challenge.KeyCodes = c.getString(c.getColumnIndex(DatabaseSchema.Challenges.KEY_CODE))
            Challenge.PuzzleNumber = c.getInt(c.getColumnIndex(DatabaseSchema.Challenges.PUZZLE_NUMBER))
            Challenge.ShakeNumber = c.getInt(c.getColumnIndex(DatabaseSchema.Challenges.SHAKE_NUMBER))
            Challenge.ShoutNumber = c.getInt(c.getColumnIndex(DatabaseSchema.Challenges.SHOUT_NUMBER))
            Challenge.BarcodeName = c.getString(c.getColumnIndex(DatabaseSchema.Challenges.BARCODE_NAME))

            c.close()
        }

        return Challenge()
    }

    fun editAlarm(alarm : Alarm) : Boolean {
        val editedAlarm = contentValuesOf()

        if (!alarm.Challenge?.let { editChallenge(it) }!!)
            return false

        editedAlarm.put(DatabaseSchema.Alarms.ALARM_TYPE, alarm.AlarmType)
        editedAlarm.put(DatabaseSchema.Alarms.IS_TEMPORARY, if (alarm.IsTemporary) 1 else 0)
        editedAlarm.put(DatabaseSchema.Alarms.SET_TIME, alarm.SetTime.toString())
        editedAlarm.put(DatabaseSchema.Alarms.REPEAT_DAYS, alarm.RepeatDays)
        editedAlarm.put(DatabaseSchema.Alarms.SOUND_NAME, alarm.SoundName)
        editedAlarm.put(DatabaseSchema.Alarms.VOLUME_LEVEL, alarm.VolumeLevel)
        editedAlarm.put(DatabaseSchema.Alarms.VIBRATION_PATTERN, alarm.VibrationPattern)
        editedAlarm.put(DatabaseSchema.Alarms.SNOOZE_DURATION, alarm.SnoozeDuration)
        editedAlarm.put(DatabaseSchema.Alarms.ALARM_NOTE, alarm.AlarmNote)

        return this.writableDatabase.update(
            DatabaseSchema.Alarms.TABLE_NAME,
            editedAlarm,
            DatabaseSchema.Alarms.ID + " = ?",
            arrayOf(alarm.Id.toString())
        ) > 0
    }

    private fun editChallenge(challenge: Challenge) : Boolean {
        val editedChallenge = contentValuesOf()

        editedChallenge.put(DatabaseSchema.Challenges.CHALLENGE_TYPE, challenge.ChallengeType)
        editedChallenge.put(DatabaseSchema.Challenges.CHALLENGE_TOUGHNESS, challenge.ChallengeToughness)

        editedChallenge.put(DatabaseSchema.Challenges.PUZZLE_NUMBER, challenge.PuzzleNumber)
        editedChallenge.put(DatabaseSchema.Challenges.SHOUT_NUMBER, challenge.ShoutNumber)
        editedChallenge.put(DatabaseSchema.Challenges.SHAKE_NUMBER, challenge.ShakeNumber)
        editedChallenge.put(DatabaseSchema.Challenges.BARCODE_NAME, challenge.BarcodeName)
        editedChallenge.put(DatabaseSchema.Challenges.KEY_CODE, challenge.KeyCodes)

        return this.writableDatabase.update(
            DatabaseSchema.Challenges.TABLE_NAME,
            editedChallenge,
            DatabaseSchema.Challenges.ID + " = ?",
            arrayOf(challenge.Id.toString())
        ) > 0
    }

    fun removeAlarm(alarm : Alarm) : Boolean {
        if (!alarm.Challenge?.let { removeChallenge(it) }!!)
            return false

        return this.writableDatabase.delete(
            DatabaseSchema.Alarms.TABLE_NAME,
            DatabaseSchema.Alarms.ID + " = ?", arrayOf(alarm.Id.toString())
        ) > 0
    }

    private fun removeChallenge(challenge: Challenge) : Boolean {
        return this.writableDatabase.delete(
            DatabaseSchema.Challenges.TABLE_NAME,
            DatabaseSchema.Challenges.ID + " = ?", arrayOf(challenge.Id.toString())
        ) > 0
    }
}