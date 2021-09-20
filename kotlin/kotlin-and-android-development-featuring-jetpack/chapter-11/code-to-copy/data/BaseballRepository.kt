package dev.mfazio.abl.data

import androidx.lifecycle.LiveData
import dev.mfazio.abl.api.services.getDefaultABLService
import dev.mfazio.abl.scoreboard.ScheduledGame
import dev.mfazio.abl.standings.TeamStanding
import dev.mfazio.abl.util.*
import java.io.IOException
import java.time.LocalDate

class BaseballRepository(
    private val baseballDatabase: BaseballDatabase,
) {
    private val baseballDao = baseballDatabase.baseballDao()

    fun getStandings(): LiveData<List<TeamStanding>> =
        baseballDao.getStandings()

    suspend fun updateStandings(): ResultStatus {
        val standingsResult = safeApiRequest {
            apiService.getStandings()
        }

        return if (
            standingsResult.success &&
            standingsResult.result?.any() == true
        ) {
            baseballDao.updateStandings(
                standingsResult.result.convertToTeamStandings(
                    baseballDao.getCurrentStandings()
                )
            )
            ResultStatus.Success
        } else {
            standingsResult.status
        }
    }

    fun getGamesForDate(date: LocalDate): LiveData<List<ScheduledGame>> =
        baseballDao.getGamesForDate("${date.toGameDateString()}%")

    suspend fun updateGamesForDate(date: LocalDate): ResultStatus {
        val gamesResult = safeApiRequest {
            apiService.getGames(requestedDate = date)
        }

        return if (gamesResult.success && gamesResult.result?.any() == true) {
            baseballDao.insertOrUpdateGames(
                gamesResult.result.convertToScheduledGames()
            )
            ResultStatus.Success
        } else {
            gamesResult.status
        }
    }

    fun getPlayerWithStats(playerId: String) = baseballDao.getPlayerWithStats(playerId)

    fun getBattersWithStats() = baseballDao.getBattersWithStats()

    fun getPitchersWithStats() = baseballDao.getPitchersWithStats()

    suspend fun updatePlayer(playerId: String): ResultStatus {
        val playerResult = safeApiRequest {
            apiService.getSinglePlayer(playerId)
        }

        return if (playerResult.success) {

            val batterPair = playerResult.result?.batting?.convertToBatterAndStats()
            val pitcherPair = playerResult.result?.pitching?.convertToPitcherAndStats()

            val player = batterPair?.first ?: pitcherPair?.first
            val playerStats = batterPair?.second ?: pitcherPair?.second

            if (player != null) {
                baseballDao.insertOrUpdatePlayer(player)
            }

            if (playerStats != null) {
                baseballDao.insertOrUpdatePlayerStats(playerStats)
            }

            ResultStatus.Success
        } else {
            playerResult.status
        }
    }

    suspend fun updateBattingLeaders(): ResultStatus {
        val leadersResult = safeApiRequest {
            apiService.getBattingLeaders()
        }

        return if (
            leadersResult.success &&
            leadersResult.result?.any() == true
        ) {
            val (players, playerStats) = leadersResult.result.convertToBattersAndStats()

            baseballDao.insertOrUpdatePlayers(players)
            baseballDao.insertOrUpdateStats(playerStats)

            ResultStatus.Success
        } else {
            leadersResult.status
        }
    }

    suspend fun updatePitchingLeaders(): ResultStatus {
        val leadersResult = safeApiRequest {
            apiService.getPitchingLeaders()
        }

        return if (
            leadersResult.success &&
            leadersResult.result?.any() == true
        ) {
            val (players, playerStats) = leadersResult.result.convertToPitchersAndStats()

            baseballDao.insertOrUpdatePlayers(players)
            baseballDao.insertOrUpdateStats(playerStats)

            ResultStatus.Success
        } else {
            leadersResult.status
        }
    }

    enum class ResultStatus {
        Unknown,
        Success,
        NetworkException,
        RequestException,
        GeneralException
    }

    inner class ApiResult<T>(
        val result: T? = null,
        val status: ResultStatus = ResultStatus.Unknown
    ) {
        val success = status == ResultStatus.Success
    }

    private suspend fun <T> safeApiRequest(
        apiFunction: suspend () -> T
    ): ApiResult<T> =
        try {
            val result = apiFunction()
            ApiResult(result, ResultStatus.Success)
        } catch (ex: retrofit2.HttpException) {
            ApiResult(status = ResultStatus.RequestException)
        } catch (ex: IOException) {
            ApiResult(status = ResultStatus.NetworkException)
        } catch (ex: Exception) {
            ApiResult(status = ResultStatus.GeneralException)
        }

    companion object {
        private val apiService = getDefaultABLService()

        @Volatile
        private var instance: BaseballRepository? = null

        fun getInstance(baseballDatabase: BaseballDatabase) =
            this.instance ?: synchronized(this) {
                instance ?: BaseballRepository(baseballDatabase).also {
                    instance = it
                }
            }
    }
}