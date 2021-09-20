package dev.mfazio.abl.players

import android.app.Application
import android.content.Intent
import android.view.View
import androidx.lifecycle.*
import dev.mfazio.abl.data.BaseballDatabase
import dev.mfazio.abl.data.BaseballRepository
import dev.mfazio.abl.util.getErrorMessage
import dev.mfazio.abl.util.toBattingPercentageString
import dev.mfazio.abl.util.toERAString
import kotlinx.coroutines.launch
import java.net.URLEncoder

class SinglePlayerViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: BaseballRepository

    private val playerId = MutableLiveData<String>()

    val playerWithStats: LiveData<PlayerWithStats>
    val stats: LiveData<List<PlayerStatWithLabel>>
    val errorMessage = MutableLiveData("")

    init {
        repo = BaseballDatabase
            .getDatabase(application, viewModelScope)
            .let { db ->
                BaseballRepository.getInstance(db)
            }

        playerWithStats = Transformations.switchMap(playerId) { playerId ->
            repo.getPlayerWithStats(playerId)
        }

        stats = Transformations.map(playerWithStats) { playerWithStats: PlayerWithStats? ->
            if (playerWithStats != null) {
                convertPlayerStatsToStatsWithLabels(
                    playerWithStats.player.position.isPitcher(),
                    playerWithStats.stats
                )
            } else listOf()
        }
    }

    fun setPlayerId(playerId: String) {
        this.playerId.value = playerId

        viewModelScope.launch {
            repo.updatePlayer(playerId).getErrorMessage(getApplication())
                ?.let { message -> errorMessage.value = message }
        }
    }

    fun sharePlayer(view: View) {
        playerWithStats.value?.player?.let { player ->
            val encodedPlayerName = URLEncoder.encode(player.fullName, "UTF-8")
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "https://link.mfazio.dev/players/${player.playerId}" +
                        "?playerName=$encodedPlayerName"
                )
                type = "text/plain"
            }

            val shareIntent =
                Intent.createChooser(sendIntent, "Share ${player.fullName}")

            view.context.startActivity(shareIntent)
        }
    }

    private fun convertPlayerStatsToStatsWithLabels(isPitcher: Boolean, playerStats: PlayerStats) =
        if (isPitcher) {
            with(playerStats.pitcherStats) {
                listOf(
                    PlayerStatWithLabel("G", this.games.toString()),
                    PlayerStatWithLabel("W-L", "${this.wins}-${this.losses}"),
                    PlayerStatWithLabel("ERA", this.era.toERAString()),
                    PlayerStatWithLabel("WHIP", this.whip.toERAString()),
                    PlayerStatWithLabel("IP", this.inningsPitched.toString()),
                    PlayerStatWithLabel("K", this.strikeouts.toString()),
                    PlayerStatWithLabel("BB", this.baseOnBalls.toString()),
                    PlayerStatWithLabel("SV", this.saves.toString())
                )
            }
        } else {
            with(playerStats.batterStats) {
                listOf(
                    PlayerStatWithLabel("G", this.games.toString()),
                    PlayerStatWithLabel("AB", this.atBats.toString()),
                    PlayerStatWithLabel("HR", this.homeRuns.toString()),
                    PlayerStatWithLabel("SB", this.stolenBases.toString()),
                    PlayerStatWithLabel("R", this.runs.toString()),
                    PlayerStatWithLabel("RBI", this.rbi.toString()),
                    PlayerStatWithLabel("BA", this.battingAverage.toBattingPercentageString()),
                    PlayerStatWithLabel("OPS", this.ops.toBattingPercentageString())
                )
            }
        }
}