package com.example.dailykitessentials.models

import com.example.dailykitessentials.helpers.EMPTY

class Challenge {

    var Id : Int = 0
    var ChallengeType : Int = 0
    var ChallengeToughness : Int = 0
    var KeyCodes : String = String.EMPTY
    var PuzzleNumber : Int = 3
    var ShakeNumber : Int = 10
    var ShoutNumber : Int = 3
    var BarcodeName : String = String.EMPTY

    var IsEditted : Boolean = false
}