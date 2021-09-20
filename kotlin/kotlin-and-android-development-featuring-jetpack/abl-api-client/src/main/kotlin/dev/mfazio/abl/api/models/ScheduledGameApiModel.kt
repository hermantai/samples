package dev.mfazio.abl.api.models

import java.time.LocalDateTime

data class ScheduledGameApiModel(
    val gameId: String,
    val gameStatus: ScheduledGameStatusApiModel,
    val gameStartTime: LocalDateTime,
    val homeTeam: ScheduledGameTeamApiModel,
    val awayTeam: ScheduledGameTeamApiModel,
    val inning: Int? = null,
    val isTopOfInning: Boolean = false,
    val outs: Int? = null,
    val occupiedBases: OccupiedBasesApiModel? = null
)

data class ScheduledGameTeamApiModel(
    val teamId: String,
    val teamNickname: String,
    val score: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0,
    val startingPitcher: ScheduledGamePitcherApiModel? = null,
    val currentPitcher: ScheduledGamePitcherApiModel? = null,
    val currentBatter: ScheduledGameBatterApiModel? = null,
    val pitcherOfRecord: ScheduledGamePitcherApiModel? = null,
    val savingPitcher: ScheduledGamePitcherApiModel? = null
)

data class ScheduledGamePitcherApiModel(
    val playerId: String,
    val playerName: String,
    val wins: Int = 0,
    val losses: Int = 0,
    val era: Double = 0.0,
    val saves: Int = 0
)

data class ScheduledGameBatterApiModel(
    val playerId: String,
    val playerName: String,
    val hitsToday: Int = 0,
    val atBatsToday: Int = 0,
    val battingAverage: Double = 0.0
)

data class OccupiedBasesApiModel(
    val first: Boolean = false,
    val second: Boolean = false,
    val third: Boolean = false
)