package dev.mfazio.abl.standings

import android.app.Application
import androidx.lifecycle.*
import dev.mfazio.abl.data.BaseballDatabase
import dev.mfazio.abl.data.BaseballRepository
import dev.mfazio.abl.util.getErrorMessage
import kotlinx.coroutines.launch

class StandingsViewModel(application: Application) :
    AndroidViewModel(application) {

    private val repo: BaseballRepository

    val standings: LiveData<List<UITeamStanding>>
    val errorMessage = MutableLiveData("")

    init {
        repo = BaseballDatabase
            .getDatabase(application, viewModelScope)
            .let { db ->
                BaseballRepository.getInstance(db)
            }

        standings =
            Transformations.map(repo.getStandings()) { teamStandings ->
                teamStandings.mapNotNull { teamStanding ->
                    UITeamStanding.fromTeamIdAndStandings(
                        teamStanding.teamId,
                        teamStandings
                    )
                }
            }
    }

    fun refreshStandings() {
        viewModelScope.launch {
            repo.updateStandings().getErrorMessage(getApplication())
                ?.let { message -> errorMessage.value = message }
        }
    }
}