package dev.mfazio.abl.players

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.mfazio.abl.data.BaseballDatabase
import dev.mfazio.abl.data.BaseballRepository
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class PlayerListViewModel(application: Application) :
    AndroidViewModel(application) {

    private val repo: BaseballRepository

    private var currentTeamId: String? = null
    private var currentNameQuery: String? = null
    private var currentPlayerListItems: Flow<PagingData<PlayerListItem>>? = null

    init {
        repo = BaseballDatabase
            .getDatabase(application, viewModelScope)
            .let { db ->
                BaseballRepository.getInstance(db)
            }
    }

    fun getPlayerListItems(
        teamId: String? = null,
        nameQuery: String? = null
    ): Flow<PagingData<PlayerListItem>> {
        val lastResult = currentPlayerListItems

        return if (teamId == currentTeamId && nameQuery == currentNameQuery && lastResult != null) {
            lastResult
        } else {
            currentNameQuery = nameQuery
            currentTeamId = teamId

            val newResult = repo
                .getPlayerListItems(teamId, nameQuery)
                .cachedIn(viewModelScope)

            currentPlayerListItems = newResult

            newResult
        }
    }
}