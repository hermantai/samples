package dev.mfazio.abl.data

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.mfazio.abl.players.Player
import dev.mfazio.abl.players.PlayerStats
import dev.mfazio.abl.scoreboard.ScheduledGame
import dev.mfazio.abl.standings.TeamStanding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        Player::class,
        PlayerStats::class,
        ScheduledGame::class,
        TeamStanding::class
    ],
    exportSchema = false,
    version = 1
)
@TypeConverters(Converters::class)
abstract class BaseballDatabase : RoomDatabase() {
    abstract fun baseballDao(): BaseballDao

    companion object {
        @Volatile
        private var Instance: BaseballDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope):
            BaseballDatabase = Instance ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context,
                BaseballDatabase::class.java,
                "BaseballDatabase"
            ).addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    scope.launch {
                        Instance
                            ?.baseballDao()
                            ?.insertStandings(TeamStanding.mockTeamStandings)
                    }
                }
            }).build()

            Instance = instance

            instance
        }
    }
}