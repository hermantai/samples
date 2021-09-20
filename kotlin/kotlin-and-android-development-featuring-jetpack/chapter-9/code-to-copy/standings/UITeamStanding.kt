package dev.mfazio.abl.standings

import dev.mfazio.abl.teams.UITeam
import java.text.DecimalFormat

data class UITeamStanding(
    val teamStanding: TeamStanding,
    val teamName: String,
    val gamesBack: Double,
    val placeInLeague: Int,
    val placeInDivision: Int
) {
    private val winPercentageNumber =
        teamStanding.wins /
                (teamStanding.wins + teamStanding.losses).toDouble()
    private val lastTenNumber =
        minOf(10 - teamStanding.winsLastTen, teamStanding.losses)

    val teamId = teamStanding.teamId
    val wins = teamStanding.wins.toString()
    val losses = teamStanding.losses.toString()
    val winPercentage = winPercentageFormat.format(winPercentageNumber)
    val winLossText = "${teamStanding.wins} - ${teamStanding.losses}"
    val gamesBackText = if (gamesBack <= 0) "-" else gamesBack.toString()
    val lastTenText = " ${teamStanding.winsLastTen}-${lastTenNumber}"
    val streakText = "${teamStanding.streakType.shortName}${teamStanding.streakCount}"

    companion object {
        private val winPercentageFormat = DecimalFormat("#.000")

        fun fromTeamIdAndStandings(teamId: String?, teamStandings: List<TeamStanding>) =
            UITeam.fromTeamId(teamId)?.let { uiTeam ->
                fromTeamAndStandings(uiTeam, teamStandings)
            }

        fun fromTeamAndStandings(team: UITeam, teamStandings: List<TeamStanding>) =
            teamStandings
                .firstOrNull { it.teamId == team.teamId }
                ?.let { teamStanding ->
                    UITeamStanding(
                        teamStanding,
                        team.teamName,
                        teamStanding.divisionGamesBack,
                        teamStandings.sortedBy { it.leagueGamesBack }.indexOf(teamStanding) + 1,
                        teamStandings
                            .filter { it.division == team.division }
                            .sortedBy { it.divisionGamesBack }
                            .indexOf(teamStanding) + 1
                    )
                }
    }
}