package dev.mfazio.abl.util

import dev.mfazio.abl.api.models.*
import dev.mfazio.abl.scoreboard.ScheduledGame
import dev.mfazio.abl.scoreboard.ScheduledGameStatus
import dev.mfazio.abl.scoreboard.ScoreboardPlayerInfo
import dev.mfazio.abl.standings.TeamStanding
import dev.mfazio.abl.standings.WinLoss
import dev.mfazio.abl.teams.Division

fun List<TeamStandingApiModel>.convertToTeamStandings(originalStandings: List<TeamStanding>): List<TeamStanding> =
    this.map { apiModel ->
        val originalStanding =
            originalStandings.firstOrNull { original -> original.teamId == apiModel.teamId }
        TeamStanding(
            apiModel.teamId,
            originalStanding?.division ?: Division.Unknown,
            apiModel.wins,
            apiModel.losses,
            apiModel.winsLastTen,
            apiModel.streakCount,
            apiModel.streakType.toWinLoss(),
            apiModel.divisionGamesBack,
            apiModel.leagueGamesBack
        ).apply { this.id = originalStanding?.id ?: 0 }
    }

fun WinLossApiModel.toWinLoss() = when (this) {
    WinLossApiModel.Win -> WinLoss.Win
    WinLossApiModel.Loss -> WinLoss.Loss
    else -> WinLoss.Unknown
}
/*
fun List<ScheduledGameApiModel>.convertToScheduledGames(): List<ScheduledGame> =
    this.map { apiModel ->
        val scoreboardPlayers = apiModel.getScoreboardPlayerInfo()
        ScheduledGame(
            apiModel.gameId,
            apiModel.gameStatus.toScheduledGameStatus(),
            apiModel.gameStartTime,
            apiModel.inning,
            apiModel.isTopOfInning,
            apiModel.outs,
            apiModel.homeTeam.teamId,
            "${apiModel.homeTeam.wins}-${apiModel.homeTeam.losses}",
            apiModel.homeTeam.score,
            apiModel.awayTeam.teamId,
            "${apiModel.awayTeam.wins}-${apiModel.awayTeam.losses}",
            apiModel.awayTeam.score,
            scoreboardPlayers.getOrNull(0),
            scoreboardPlayers.getOrNull(1),
            scoreboardPlayers.getOrNull(2)
        )
    }

fun ScheduledGameStatusApiModel.toScheduledGameStatus() =
    when (this) {
        ScheduledGameStatusApiModel.Upcoming -> ScheduledGameStatus.Upcoming
        ScheduledGameStatusApiModel.InProgress -> ScheduledGameStatus.InProgress
        ScheduledGameStatusApiModel.Completed -> ScheduledGameStatus.Completed
        else -> ScheduledGameStatus.Unknown
    }

fun ScheduledGameApiModel.getScoreboardPlayerInfo(): List<ScoreboardPlayerInfo?> =
    when (this.gameStatus) {
        ScheduledGameStatusApiModel.Completed -> {
            val homeTeamWon = this.homeTeam.score > this.awayTeam.score
            listOf(
                (if (homeTeamWon) this.homeTeam.pitcherOfRecord else this.awayTeam.pitcherOfRecord)?.convertPitcherToScoreboardPlayerInfo(),
                (if (homeTeamWon) this.awayTeam.pitcherOfRecord else this.homeTeam.pitcherOfRecord)?.convertPitcherToScoreboardPlayerInfo(),
                (if (homeTeamWon) this.homeTeam.savingPitcher else this.awayTeam.savingPitcher)?.convertCloserToScoreboardPlayerInfo()
            )
        }
        ScheduledGameStatusApiModel.InProgress -> listOf(
            (if (this.isTopOfInning) this.homeTeam.currentPitcher else this.awayTeam.currentPitcher)?.convertPitcherToScoreboardPlayerInfo(),
            (if (this.isTopOfInning) this.awayTeam.currentBatter else this.homeTeam.currentBatter)?.convertCurrentBatterToScoreboardPlayerInfo()
        )
        ScheduledGameStatusApiModel.Upcoming -> listOf(
            this.awayTeam.startingPitcher?.convertPitcherToScoreboardPlayerInfo(),
            this.homeTeam.startingPitcher?.convertPitcherToScoreboardPlayerInfo()
        )
        else -> emptyList()
    }

fun ScheduledGamePitcherApiModel.convertPitcherToScoreboardPlayerInfo() =
    ScoreboardPlayerInfo(this.playerName, "${this.wins}-${this.losses}, ${this.era}")

fun ScheduledGamePitcherApiModel.convertCloserToScoreboardPlayerInfo() =
    ScoreboardPlayerInfo(this.playerName, this.saves.toString())

fun ScheduledGameBatterApiModel.convertCurrentBatterToScoreboardPlayerInfo() =
    ScoreboardPlayerInfo(
        this.playerName,
        "${this.hitsToday}-${this.atBatsToday}, ${this.battingAverage}"
    )
*/