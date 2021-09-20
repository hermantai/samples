package dev.mfazio.abl.api.models

data class BatterBoxScoreItemApiModel(
    val playerId: String,
    val teamId: String,
    val firstName: String,
    val lastName: String,
    val number: Int = 0,
    val bats: HandApiModel = HandApiModel.Right,
    val throws: HandApiModel = HandApiModel.Right,
    val position: PositionApiModel = PositionApiModel.Unknown,
    val boxScoreLastName: String?,
    val games: Int,
    val plateAppearances: Int,
    val atBats: Int,
    val runs: Int,
    val hits: Int,
    val doubles: Int,
    val triples: Int,
    val homeRuns: Int,
    val rbi: Int,
    val strikeouts: Int,
    val baseOnBalls: Int,
    val hitByPitch: Int,
    val stolenBases: Int,
    val caughtStealing: Int,
    val gidp: Int,
    val sacrificeHits: Int,
    val sacrificeFlies: Int,
    val errors: Int = 0,
    val passedBalls: Int = 0
)