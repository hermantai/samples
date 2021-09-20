package dev.mfazio.abl

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import dev.mfazio.abl.databinding.ActivityMainBinding
import dev.mfazio.abl.notifications.ABLNotificationChannel
import dev.mfazio.abl.settings.SettingsFragment
import dev.mfazio.abl.settings.getSelectedStartingScreen
import dev.mfazio.abl.teams.UITeam

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.containerFragment) as NavHostFragment
        this.navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        navGraph.startDestination = getSelectedStartingScreen(prefs)
        navController.graph = navGraph

        binding.navView.setupWithNavController(this.navController)

        this.appBarConfiguration = AppBarConfiguration(binding.navView.menu, binding.drawerLayout)
        setupActionBarWithNavController(this.navController, appBarConfiguration)

        if (prefs.getBoolean(SettingsFragment.favoriteTeamColorsPreferenceKey, false)) {
            UITeam.getTeamPalette(
                this,
                prefs.getString(SettingsFragment.favoriteTeamPreferenceKey, null)
            )?.dominantSwatch?.rgb?.let {
                window.navigationBarColor = it
            }
        }

        createNotificationChannels()
    }

    override fun onSupportNavigateUp(): Boolean =
        (this::navController.isInitialized &&
            this.navController.navigateUp(this.appBarConfiguration)
            ) || super.onSupportNavigateUp()

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = ABLNotificationChannel.values().map { channel ->
                NotificationChannel(
                    channel.channelId,
                    channel.channelName,
                    channel.importance
                ).apply {
                    description = channel.channelDescription
                }
            }

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannels(channels)
        }
    }
}