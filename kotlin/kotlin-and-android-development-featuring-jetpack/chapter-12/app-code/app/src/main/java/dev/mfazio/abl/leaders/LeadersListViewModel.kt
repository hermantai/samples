package dev.mfazio.abl.leaders

import android.app.Application
import androidx.lifecycle.*
import dev.mfazio.abl.data.BaseballDatabase
import dev.mfazio.abl.data.BaseballRepository
import dev.mfazio.abl.players.PlayerWithStats
import dev.mfazio.abl.teams.UITeam
import dev.mfazio.abl.util.getErrorMessage
import dev.mfazio.abl.util.toBattingPercentageString
import dev.mfazio.abl.util.toERAString
import kotlinx.coroutines.launch

class LeadersListViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: BaseballRepository

    val battingLeaders: LiveData<List<LeaderListItem>>
    val pitchingLeaders: LiveData<List<LeaderListItem>>
    val errorMessage = MutableLiveData("")

    init {
        repo = BaseballDatabase
            .getDatabase(application, viewModelScope)
            .let { db ->
                BaseballRepository.getInstance(db)
            }

        battingLeaders = Transformations.map(repo.getBattersWithStats()) { battersWithStats ->
            battingStatCategories.mapIndexed { index, handler ->
                battersWithStats
                    ?.sortedWith(handler.statComparator)
                    ?.firstOrNull()?.let { batterWithStats ->
                        LeaderListItem(
                            index.toLong(),
                            batterWithStats.player,
                            handler.category,
                            handler.formattedStatValue(batterWithStats),
                            UITeam.fromTeamId(batterWithStats.player.teamId)?.nickname ?: "N/A"
                        )
                    }
            }.filterNotNull()
        }

        pitchingLeaders = Transformations.map(repo.getPitchersWithStats()) { pitchersWithStats ->
            pitchingStatCategories.mapIndexed { index, handler ->
                pitchersWithStats
                    ?.sortedWith(handler.statComparator)
                    ?.firstOrNull()?.let { pitcherWithStats ->
                        LeaderListItem(
                            index.toLong(),
                            pitcherWithStats.player,
                            handler.category,
                            handler.formattedStatValue(pitcherWithStats),
                            UITeam.fromTeamId(pitcherWithStats.player.teamId)?.nickname ?: "N/A"
                        )
                    }
            }.filterNotNull()
        }

        refreshBattingLeaders()
        refreshPitchingLeaders()
    }

    fun refreshBattingLeaders() {
        refreshLeaders { repo.updateBattingLeaders() }
    }

    fun refreshPitchingLeaders() {
        refreshLeaders { repo.updatePitchingLeaders() }
    }

    private fun refreshLeaders(updateFunction: suspend () -> BaseballRepository.ResultStatus) {
        viewModelScope.launch {
            updateFunction().getErrorMessage(getApplication())
                ?.let { message -> errorMessage.value = message }
        }
    }

    private val battingStatCategories = listOf(
        PlayerStatHandler(
            "AVG",
            { batterA, batterB -> -batterA.stats.batterStats.battingAverage.compareTo(batterB.stats.batterStats.battingAverage) },
            { batter -> batter.stats.batterStats.battingAverage.toBattingPercentageString() }
        ),
        PlayerStatHandler(
            "HR",
            { batterA, batterB -> -batterA.stats.batterStats.homeRuns.compareTo(batterB.stats.batterStats.homeRuns) },
            { batter -> batter.stats.batterStats.homeRuns.toString() }
        ),
        PlayerStatHandler(
            "RBI",
            { batterA, batterB -> -batterA.stats.batterStats.rbi.compareTo(batterB.stats.batterStats.rbi) },
            { batter -> batter.stats.batterStats.rbi.toString() }
        ),
        PlayerStatHandler(
            "SB",
            { batterA, batterB -> -batterA.stats.batterStats.stolenBases.compareTo(batterB.stats.batterStats.stolenBases) },
            { batter -> batter.stats.batterStats.stolenBases.toString() }
        ),
        PlayerStatHandler(
            "R",
            { batterA, batterB -> -batterA.stats.batterStats.runs.compareTo(batterB.stats.batterStats.runs) },
            { batter -> batter.stats.batterStats.runs.toString() }
        ),
        PlayerStatHandler(
            "OBP",
            { batterA, batterB -> -batterA.stats.batterStats.onBasePercentage.compareTo(batterB.stats.batterStats.onBasePercentage) },
            { batter -> batter.stats.batterStats.onBasePercentage.toBattingPercentageString() }
        ),
        PlayerStatHandler(
            "SLG",
            { batterA, batterB -> -batterA.stats.batterStats.sluggingPercentage.compareTo(batterB.stats.batterStats.sluggingPercentage) },
            { batter -> batter.stats.batterStats.sluggingPercentage.toBattingPercentageString() }
        ),
        PlayerStatHandler(
            "OPS",
            { batterA, batterB -> -batterA.stats.batterStats.ops.compareTo(batterB.stats.batterStats.ops) },
            { batter -> batter.stats.batterStats.ops.toBattingPercentageString() }
        )
    )
    private val pitchingStatCategories = listOf(
        PlayerStatHandler(
            "ERA",
            { pitcherA, pitcherB -> pitcherA.stats.pitcherStats.era.compareTo(pitcherB.stats.pitcherStats.era) },
            { pitcher -> pitcher.stats.pitcherStats.era.toERAString() }
        ),
        PlayerStatHandler(
            "W",
            { pitcherA, pitcherB -> -pitcherA.stats.pitcherStats.wins.compareTo(pitcherB.stats.pitcherStats.wins) },
            { pitcher -> pitcher.stats.pitcherStats.wins.toString() }
        ),
        PlayerStatHandler(
            "L",
            { pitcherA, pitcherB -> pitcherA.stats.pitcherStats.losses.compareTo(pitcherB.stats.pitcherStats.losses) },
            { pitcher -> pitcher.stats.pitcherStats.losses.toString() }
        ),
        PlayerStatHandler(
            "SO",
            { pitcherA, pitcherB -> -pitcherA.stats.pitcherStats.strikeouts.compareTo(pitcherB.stats.pitcherStats.strikeouts) },
            { pitcher -> pitcher.stats.pitcherStats.strikeouts.toString() }
        ),
        PlayerStatHandler(
            "SV",
            { pitcherA, pitcherB -> -pitcherA.stats.pitcherStats.saves.compareTo(pitcherB.stats.pitcherStats.saves) },
            { pitcher -> pitcher.stats.pitcherStats.saves.toString() }
        ),
        PlayerStatHandler(
            "WHIP",
            { pitcherA, pitcherB -> pitcherA.stats.pitcherStats.whip.compareTo(pitcherB.stats.pitcherStats.whip) },
            { pitcher -> pitcher.stats.pitcherStats.whip.toERAString() }
        ),
        PlayerStatHandler(
            "IP",
            { pitcherA, pitcherB -> -pitcherA.stats.pitcherStats.inningsPitched.compareTo(pitcherB.stats.pitcherStats.inningsPitched) },
            { pitcher -> pitcher.stats.pitcherStats.inningsPitched.toString() }
        )
    )

    private data class PlayerStatHandler(
        val category: String,
        val statComparator: Comparator<PlayerWithStats>,
        val formattedStatValue: (PlayerWithStats) -> String
    )
}