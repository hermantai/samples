package dev.mfazio.abl.teams

import android.content.Intent
import android.view.View
import androidx.lifecycle.*
import dev.mfazio.abl.standings.TeamStanding
import dev.mfazio.abl.standings.UITeamStanding
import java.net.URLEncoder

class SingleTeamViewModel : ViewModel() {

    val team = MutableLiveData<UITeam>()

    private val standings: LiveData<List<TeamStanding>>

    val teamStanding = MediatorLiveData<UITeamStanding>()

    init {
        standings = MutableLiveData<List<TeamStanding>>().apply {
            value = TeamStanding.mockTeamStandings
        }

        teamStanding.addSource(this.team) { uiTeam ->
            updateUIStanding(uiTeam, standings.value)
        }

        teamStanding.addSource(this.standings) { standings ->
            updateUIStanding(team.value, standings)
        }
    }

    fun setTeam(teamId: String) {
        UITeam.allTeams.firstOrNull { it.teamId == teamId }?.let { team ->
            this.team.value = team
        }
    }

    fun shareTeam(view: View) {
        team.value?.let { team ->
            val encodedTeamName = URLEncoder.encode(team.teamName, "UTF-8")
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "https://link.mfazio.dev/teams/${team.teamId}" +
                        "?teamName=$encodedTeamName"
                )
                type = "text/plain"
            }

            val shareIntent =
                Intent.createChooser(sendIntent, "Share ${team.teamName}")

            view.context.startActivity(shareIntent)
        }
    }

    private fun updateUIStanding(uiTeam: UITeam?, teamStandings: List<TeamStanding>?) {
        if(uiTeam != null && teamStandings != null) {
            this.teamStanding.value = UITeamStanding.fromTeamAndStandings(uiTeam, teamStandings)
        }
    }
}