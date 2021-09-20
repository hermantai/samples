package dev.mfazio.abl.standings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StandingsViewModel: ViewModel() {
    val standings: LiveData<List<UITeamStanding>> = MutableLiveData(
        TeamStanding.mockTeamStandings.mapNotNull { teamStanding ->
            UITeamStanding.fromTeamIdAndStandings(
                teamStanding.teamId,
                TeamStanding.mockTeamStandings
            )
        }
    )
}