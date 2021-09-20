package dev.mfazio.abl.api.services

import dev.mfazio.abl.api.models.*
import retrofit2.http.*
import java.time.LocalDate
import java.time.LocalDateTime

interface AndroidBaseballLeagueService {

    @GET("teams")
    suspend fun getTeams(): List<TeamApiModel>

    @GET("games")
    suspend fun getGames(
        @Query("currentDateTime") currentDateTime: LocalDateTime? = null,
        @Query("requestedDate") requestedDate: LocalDate? = null,
        @Query("teamId") teamId: String? = null
    ): List<ScheduledGameApiModel>

    @GET("players")
    suspend fun getPlayers(
        @Query("pageNumber") pageNumber: Int? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("query") query: String? = null,
        @Query("teamId") teamId: String? = null,
        @Query("position") position: PositionApiModel? = null,
        @Query("isPitcher") isPitcher: Boolean? = null,
        @Query("isOutfielder") isOutfielder: Boolean? = null
    ): List<PlayerApiModel>

    @GET("players/{playerId}")
    suspend fun getSinglePlayer(
        @Path("playerId") playerId: String,
        @Query("currentDate") currentDate: LocalDate? = null
    ): BoxScoreItemsApiModel

    @GET("standings")
    suspend fun getStandings(@Query("currentDate") currentDate: LocalDate? = null): List<TeamStandingApiModel>

    @GET("stats/batting")
    suspend fun getBattingStats(
        @Query("currentDate") currentDate: LocalDate? = null,
        @Query("pageNumber") pageNumber: Int? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("sortStat") sortStat: String? = null,
        @Query("sortDescending") sortDescending: Boolean? = null,
        @Query("teamId") teamId: String? = null,
        @Query("position") position: PositionApiModel? = null
    ): List<BatterBoxScoreItemApiModel>

    @GET("stats/pitching")
    suspend fun getPitchingStats(
        @Query("currentDate") currentDate: LocalDate? = null,
        @Query("pageNumber") pageNumber: Int? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("sortStat") sortStat: String? = null,
        @Query("sortDescending") sortDescending: Boolean? = null,
        @Query("teamId") teamId: String? = null,
        @Query("position") position: PositionApiModel? = null
    ): List<PitcherBoxScoreItemApiModel>

    @GET("leaders/batting")
    suspend fun getBattingLeaders(
        @Query("currentDate") currentDate: LocalDate? = null
    ): List<BatterBoxScoreItemApiModel>

    @GET("leaders/pitching")
    suspend fun getPitchingLeaders(
        @Query("currentDate") currentDate: LocalDate? = null
    ): List<PitcherBoxScoreItemApiModel>

    @POST("app/notifications")
    suspend fun sendNotificationToPhone(
        @Query("notificationType") notificationType: NotificationTypeApiModel,
        @Query("phoneToken") phoneToken: String,
        @Query("itemId") itemId: String,
        @Query("dataOnly") dataOnly: Boolean = false
    )

    @GET("app/settings")
    suspend fun getAppSettingsForUser(
        @Query("userId") userId: String
    ): AppSettingsApiModel

    @POST("app/settings")
    suspend fun saveAppSettings(
        @Body settings: AppSettingsApiModel
    ): AppSettingsApiModel
}