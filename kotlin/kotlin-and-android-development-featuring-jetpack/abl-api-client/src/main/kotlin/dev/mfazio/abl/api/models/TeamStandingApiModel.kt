package dev.mfazio.abl.api.models

data class TeamStandingApiModel(
    val teamId: String,
    val wins: Int,
    val losses: Int,
    val winsLastTen: Int,
    val streakCount: Int,
    val streakType: WinLossApiModel,
    val divisionGamesBack: Double,
    val leagueGamesBack: Double
)