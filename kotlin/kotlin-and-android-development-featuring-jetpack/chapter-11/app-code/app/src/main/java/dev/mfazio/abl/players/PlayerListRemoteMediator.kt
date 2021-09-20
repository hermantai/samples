package dev.mfazio.abl.players

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.mfazio.abl.api.services.AndroidBaseballLeagueService
import dev.mfazio.abl.data.BaseballDatabase
import dev.mfazio.abl.util.convertToPlayers
import java.io.InvalidObjectException

@ExperimentalPagingApi
class PlayerListRemoteMediator(
    private val apiService: AndroidBaseballLeagueService,
    private val baseballDatabase: BaseballDatabase,
    private val teamId: String? = null,
    private val nameQuery: String? = null
) : RemoteMediator<Int, PlayerListItem>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PlayerListItem>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.PREPEND -> {
                val keys = loadKeysForFirstPlayer(state)
                    ?: return MediatorResult.Error(
                        InvalidObjectException("Keys should not be null for $loadType.")
                    )

                keys.previousKey
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                val keys = loadKeysForLastPlayer(state)

                keys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.REFRESH -> {
                val keys = loadKeysForClosestPlayer(state)

                keys?.nextKey ?: startingPageIndex
            }
        }

        return loadAndSaveApiData(page, state, loadType == LoadType.REFRESH)
    }

    private suspend fun loadAndSaveApiData(
        page: Int,
        state: PagingState<Int, PlayerListItem>,
        isRefresh: Boolean
    ): MediatorResult =
        try {
            val apiResponse = apiService.getPlayers(
                page,
                state.config.pageSize,
                nameQuery,
                teamId
            )

            val players = apiResponse.convertToPlayers()

            val endOfPaginationReached = players.isEmpty()

            baseballDatabase.withTransaction {
                if (isRefresh) {
                    baseballDatabase.playerKeysDao().deleteAllPlayerKeys()
                    baseballDatabase.baseballDao().deleteAllPlayerListItems()
                }

                val previousKey = if (page == startingPageIndex) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = players.map { player ->
                    PlayerKeys(player.playerId, previousKey, nextKey)
                }

                baseballDatabase.playerKeysDao().insertKeys(keys)
                baseballDatabase.baseballDao().insertOrUpdatePlayers(players)
                baseballDatabase.baseballDao().insertPlayerListItems(
                    players.map { player ->
                        PlayerListItem(
                            player.playerId,
                            player.fullName,
                            player.teamId,
                            player.position
                        )
                    }
                )

                MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            }

        } catch (ex: Exception) {
            MediatorResult.Error(ex)
        }

    private suspend fun loadKeysForFirstPlayer(
        state: PagingState<Int, PlayerListItem>
    ) = state.pages.firstOrNull { it.data.isNotEmpty() }
        ?.data?.firstOrNull()?.let { player ->
            baseballDatabase.playerKeysDao().getPlayerKeysByPlayerId(player.playerId)
        }

    private suspend fun loadKeysForLastPlayer(
        state: PagingState<Int, PlayerListItem>
    ) = state.pages.lastOrNull { it.data.isNotEmpty() }
        ?.data?.lastOrNull()?.let { player ->
            baseballDatabase.playerKeysDao().getPlayerKeysByPlayerId(player.playerId)
        }

    private suspend fun loadKeysForClosestPlayer(
        state: PagingState<Int, PlayerListItem>
    ) = state.anchorPosition?.let { position ->
        state.closestItemToPosition(position)?.playerId?.let { playerId ->
            baseballDatabase.playerKeysDao().getPlayerKeysByPlayerId(playerId)
        }
    }

    companion object {
        private const val startingPageIndex = 0
    }
}