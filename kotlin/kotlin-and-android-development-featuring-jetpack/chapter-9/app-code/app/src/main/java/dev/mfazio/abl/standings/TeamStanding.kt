package dev.mfazio.abl.standings

import dev.mfazio.abl.teams.Division
import dev.mfazio.abl.teams.Team

data class TeamStanding(
    val teamId: String,
    val division: Division,
    val wins: Int,
    val losses: Int,
    val winsLastTen: Int,
    val streakCount: Int,
    val streakType: WinLoss,
    val divisionGamesBack: Double,
    val leagueGamesBack: Double
) {
    companion object {
        val mockTeamStandings = listOf(
            TeamStanding(Team.Appleton.id, Team.Appleton.division, 80, 75, 6, 1, WinLoss.Loss, 15.5, 15.5),
            TeamStanding(Team.Baraboo.id, Team.Baraboo.division, 78, 78, 5, 1, WinLoss.Loss, 16.0, 18.0),
            TeamStanding(Team.EauClaire.id, Team.EauClaire.division, 73, 83, 4, 1, WinLoss.Win, 21.0, 23.0),
            TeamStanding(Team.GreenBay.id, Team.GreenBay.division, 63, 93, 1, 7, WinLoss.Loss, 33.0, 33.0),
            TeamStanding(Team.LaCrosse.id, Team.LaCrosse.division, 65, 90, 6, 1, WinLoss.Loss, 28.5, 30.5),
            TeamStanding(Team.LakeDelton.id, Team.LakeDelton.division, 67, 89, 3, 3, WinLoss.Loss, 27.0, 29.0),
            TeamStanding(Team.Milwaukee.id, Team.Milwaukee.division, 91, 64, 6, 1, WinLoss.Loss, 4.5, 4.5),
            TeamStanding(Team.Madison.id, Team.Madison.division, 94, 62, 5, 4, WinLoss.Win, 0.0, 2.0),
            TeamStanding(Team.Pewaukee.id, Team.Pewaukee.division, 93, 63, 4, 1, WinLoss.Loss, 3.0, 3.0),
            TeamStanding(Team.SturgeonBay.id, Team.SturgeonBay.division, 75, 80, 7, 1, WinLoss.Win, 20.5, 20.5),
            TeamStanding(Team.SpringGreen.id, Team.SpringGreen.division, 55, 100, 5, 1, WinLoss.Win, 38.5, 40.5),
            TeamStanding(Team.Shawano.id, Team.Shawano.division, 72, 84, 6, 1, WinLoss.Win, 24.0, 24.0),
            TeamStanding(Team.Waukesha.id, Team.Waukesha.division, 96, 60, 6, 3, WinLoss.Win, 0.0, 0.0),
            TeamStanding(Team.WisconsinRapids.id, Team.WisconsinRapids.division, 87, 68, 7, 1, WinLoss.Win, 6.5, 8.5)
        )
    }
}

enum class WinLoss { 
    Win, Loss, Unknown;
    
    val shortName = this.name.substring(0, 1)
}