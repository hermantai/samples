package dev.mfazio.abl.scoreboard

import android.app.Application
import androidx.lifecycle.*
import dev.mfazio.abl.data.BaseballDatabase
import dev.mfazio.abl.data.BaseballRepository
import dev.mfazio.abl.teams.UITeam
import dev.mfazio.abl.util.getErrorMessage
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ScoreboardViewModel(application: Application) : AndroidViewModel(application) {
    private val currentDateTextFormat = DateTimeFormatter.ofPattern("EEEE, MMM d")

    private val repo: BaseballRepository

    private val selectedDate = MutableLiveData(LocalDate.now())

    val games: LiveData<List<ScheduledGame>>
    val teams: LiveData<List<UITeam>>
    val currentDateText: LiveData<String>
    val errorMessage = MutableLiveData("")

    init {
        repo = BaseballDatabase
            .getDatabase(application, viewModelScope)
            .baseballDao()
            .let { dao ->
                BaseballRepository.getInstance(dao)
            }

        games = Transformations.switchMap(selectedDate) { selectedDate ->
            refreshScores(selectedDate)
            repo.getGamesForDate(selectedDate)
        }

        teams = Transformations.map(games) { scheduledGames ->
            scheduledGames.flatMap { game ->
                UITeam.fromTeamIds(game.homeTeamId, game.awayTeamId)
            }.filterNotNull()
        }

        currentDateText = Transformations.map(selectedDate) { currentDate ->
            currentDateTextFormat.format(currentDate)
        }
    }

    fun goToDate(daysToMove: Long = 0, monthsToMove: Long? = null) {
        selectedDate.value?.let { date ->
            selectedDate.value = if (monthsToMove != null) {
                date.plusMonths(monthsToMove)
            } else {
                date.plusDays(daysToMove)
            }
        }
    }

    private fun refreshScores(date: LocalDate) {
        //TODO:Add this in once the Repository has updateGamesForDate(...)
        /*viewModelScope.launch {
            repo.updateGamesForDate(date).getErrorMessage(getApplication())?.let { message ->
                errorMessage.value = message
            }
        }*/
    }
}