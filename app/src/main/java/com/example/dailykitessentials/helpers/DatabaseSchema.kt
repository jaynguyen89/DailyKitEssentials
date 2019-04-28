package com.example.dailykitessentials.helpers

class DatabaseSchema {

    class Alarms constructor() {
        companion object {
            operator fun invoke(): Alarms {
                return Alarms()
            }

            const val TABLE_NAME = "Alarms"

            const val ID = "Id"

            // Common fields for all alarms
            const val ALARM_TYPE = "AlarmType" // Normal: 0, Tough: 1
            const val IS_TEMPORARY = "IsTemporary" // Quick alarm. Temporary: 1, Repeat: 0
            const val SET_TIME = "SetTime"
            const val REPEAT_DAYS = "RepeatDays" // Mon - Sun
            const val SOUND_NAME = "SoundName"
            const val VOLUME_LEVEL = "VolumeLevel"
            const val VIBRATION_PATTERN = "VibrationPattern"
            const val SNOOZE_DURATION = "SnoozeDuration"
            const val ALARM_NOTE = "AlarmNote"
            const val IS_ACTIVE = "IsActive"

            // Foreign key to Challenges table, default NULL for normal alarm
            const val CHALLENGE_ID = "ChallengeId"

            // CREATE_TABLE statement
            const val CREATE_TABLE_STATEMENT = "CREATE TABLE " +
                    TABLE_NAME + " (" +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        CHALLENGE_ID + " INTEGER DEFAULT NULL," +
                        ALARM_TYPE + " INTEGER DEFAULT 0," +
                        IS_TEMPORARY + " INTEGER DEFAULT 0," +
                        SET_TIME + " TEXT NOT NULL," +
                        REPEAT_DAYS + " TEXT DEFAULT \"\"," +
                        SOUND_NAME + " TEXT NOT NULL," +
                        VOLUME_LEVEL + " INTEGER DEFAULT 10," +
                        VIBRATION_PATTERN + " TEXT DEFAULT \"\"," +
                        SNOOZE_DURATION + " INTEGER DEFAULT 5," +
                        ALARM_NOTE + " TEXT DEFAULT \"\"," +
                        IS_ACTIVE + " INTEGER DEFAULT 0," +
                        "FOREIGN KEY (" + CHALLENGE_ID + ") REFERENCES " + Challenges.TABLE_NAME + "(" + Challenges.ID + ") ON DELETE CASCADE" +
                    ");"
        }
    }

    class Challenges constructor() {
        companion object {
            operator fun invoke(): Challenges {
                return Challenges()
            }

            const val TABLE_NAME = "Challenges"

            // Common fields for all challenges
            const val ID = "Id"
            const val CHALLENGE_TYPE = "ChallengeType" // Puzzle: 0, Shout: 1, Shake: 2, Barcode: 3, KeyCode: 4
            const val CHALLENGE_TOUGHNESS = "ChallengeToughness" // Easy: 0, Medium: 1, Hard: 2, Extreme: 3

            // Specific fields for each challenge type
            const val KEY_CODE = "KeyCodes"
            const val PUZZLE_NUMBER = "PuzzleNumber" // Min 3
            const val SHAKE_NUMBER = "ShakeNumber" // Min 10
            const val SHOUT_NUMBER = "ShoutNumber" // Min 1
            const val BARCODE_NAME = "BarcodeName"

            // CREATE_TABLE statement
            const val CREATE_TABLE_STATEMENT = "CREATE TABLE " +
                    TABLE_NAME + " (" +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        CHALLENGE_TYPE + " INTEGER DEFAULT 0," +
                        CHALLENGE_TOUGHNESS + " INTEGER DEFAULT 0," +
                        KEY_CODE + " TEXT DEFAULT \"\"," +
                        PUZZLE_NUMBER + " INTEGER DEFAULT NULL," +
                        SHAKE_NUMBER + " INTEGER DEFAULT NULL," +
                        SHOUT_NUMBER + " INTEGER DEFAULT NULL," +
                        BARCODE_NAME + " TEXT DEFAULT \"\"" +
                    ");"
        }
    }
}
