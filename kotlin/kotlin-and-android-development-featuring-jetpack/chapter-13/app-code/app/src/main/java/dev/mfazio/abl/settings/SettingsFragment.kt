package dev.mfazio.abl.settings

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.preference.*
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.mfazio.abl.R
import dev.mfazio.abl.api.services.getDefaultABLService
import dev.mfazio.abl.teams.UITeam
import kotlinx.coroutines.launch

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var favoriteTeamPreference: DropDownPreference
    private lateinit var favoriteTeamColorPreference: SwitchPreferenceCompat
    private lateinit var startingScreenPreference: DropDownPreference
    private lateinit var usernamePreference: EditTextPreference

    override fun onCreatePreferences(
        savedInstanceState: Bundle?,
        rootKey: String?
    ) {
        val ctx = preferenceManager.context
        val screen = preferenceManager.createPreferenceScreen(ctx)

        this.usernamePreference = EditTextPreference(ctx).apply {
            key = usernamePreferenceKey
            title = getString(R.string.user_name)
            summaryProvider = EditTextPreference.SimpleSummaryProvider.getInstance()
            onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _, newValue ->
                    loadSettings(newValue?.toString())

                    true
                }
        }
        screen.addPreference(usernamePreference)

        this.favoriteTeamPreference = DropDownPreference(ctx).apply {
            key = favoriteTeamPreferenceKey
            title = getString(R.string.favorite_team)
            entries = 
                (listOf(getString(R.string.none)) + UITeam.allTeams.map { it.teamName }).toTypedArray()
            entryValues = (listOf("") + UITeam.allTeams.map { it.teamId }).toTypedArray()
            setDefaultValue("")
            summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
        }

        this.favoriteTeamColorPreference = SwitchPreferenceCompat(ctx).apply {
            key = favoriteTeamColorsPreferenceKey
            title = getString(R.string.team_color_nav_bar)
            setDefaultValue(false)
        }

        favoriteTeamPreference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                val teamId = newValue?.toString()

                if (favoriteTeamColorPreference.isChecked) {
                    setNavBarColorForTeam(teamId)
                }

                if (teamId != null) {
                    favoriteTeamPreference.icon = getIconForTeam(teamId)
                }

                saveSettings(favoriteTeam = teamId)

                true
            }

        screen.addPreference(favoriteTeamPreference)

        favoriteTeamColorPreference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                val useFavoriteTeamColor = newValue as? Boolean

                setNavBarColorForTeam(
                    if (useFavoriteTeamColor == true) {
                        favoriteTeamPreference.value
                    } else null
                )

                saveSettings(useFavoriteTeamColor = useFavoriteTeamColor)

                true
            }

        screen.addPreference(favoriteTeamColorPreference)

        this.startingScreenPreference = DropDownPreference(ctx).apply {
            key = startingScreenPreferenceKey
            title = getString(R.string.starting_location)
            entries = startingScreens.keys.toTypedArray()
            entryValues = startingScreens.keys.toTypedArray()
            summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
            setDefaultValue(R.id.scoreboardFragment.toString())
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                saveSettings(startingScreen = newValue?.toString())
                true
            }
        }
        screen.addPreference(startingScreenPreference)

        val aboutCategory = PreferenceCategory(ctx).apply {
            key = aboutPreferenceCategoryKey
            title = getString(R.string.about)
        }
        screen.addPreference(aboutCategory)

        val aboutTheAppPreference = Preference(ctx).apply {
            key = aboutTheAppPreferenceKey
            title = getString(R.string.about_the_app)
            summary = getString(R.string.info_about_the_app)
            onPreferenceClickListener = Preference.OnPreferenceClickListener {
                this@SettingsFragment.findNavController().navigate(
                    R.id.aboutTheAppFragment
                )
                true
            }
        }
        aboutCategory.addPreference(aboutTheAppPreference)

        val creditsPreference = Preference(ctx).apply {
            key = creditsPreferenceKey
            title = getString(R.string.credits)
            summary = getString(R.string.image_credits)
            onPreferenceClickListener = Preference.OnPreferenceClickListener {
                this@SettingsFragment.findNavController().navigate(
                    R.id.imageCreditsFragment
                )
                true
            }
        }
        aboutCategory.addPreference(creditsPreference)

        val notificationsPreference = Preference(ctx).apply {
            key = notificationsPreferenceKey
            title = "Notifications"
            summary = "Links to display push and local notifications"
            onPreferenceClickListener = Preference.OnPreferenceClickListener {
                this@SettingsFragment.findNavController().navigate(
                    R.id.notificationsFragment
                )
                true
            }
        }
        aboutCategory.addPreference(notificationsPreference)

        preferenceScreen = screen
    }

    override fun onBindPreferences() {
        favoriteTeamPreference.icon = getIconForTeam(favoriteTeamPreference.value)
    }

    private fun getIconForTeam(teamId: String) =
        UITeam.fromTeamId(teamId)?.let { team ->
            ContextCompat.getDrawable(requireContext(), team.logoId)
        }

    private fun setNavBarColorForTeam(teamId: String?) {
        val color = UITeam.getTeamPalette(requireContext(), teamId)
            ?.dominantSwatch
            ?.rgb
            ?: getDefaultColor()

        activity?.window?.navigationBarColor = color
    }

    private fun getDefaultColor(): Int {
        val colorValue = TypedValue()

        activity?.theme?.resolveAttribute(
            R.attr.colorPrimary,
            colorValue,
            true
        )

        return colorValue.data
    }

    private fun saveSettings(
        userName: String? = usernamePreference.text,
        favoriteTeam: String? = favoriteTeamPreference.value,
        useFavoriteTeamColor: Boolean? = favoriteTeamColorPreference.isChecked,
        startingScreen: String? = startingScreenPreference.value
    ) {
        if (userName != null) {
            WorkManager.getInstance(requireContext()).enqueue(
                OneTimeWorkRequestBuilder<SaveSettingsWorker>()
                    .setInputData(
                        workDataOf(
                            SaveSettingsWorker.userNameKey to userName,
                            SaveSettingsWorker.favoriteTeamKey to favoriteTeam,
                            SaveSettingsWorker.favoriteTeamColorCheckKey to useFavoriteTeamColor,
                            SaveSettingsWorker.startingScreenKey to startingScreen
                        )
                    ).build()
            )
        }
    }

    private fun loadSettings(userName: String? = null) {
        if (userName != null) {
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val apiResult = getDefaultABLService().getAppSettingsForUser(userName)

                    with(favoriteTeamPreference) {
                        value = apiResult.favoriteTeamId
                        icon = getIconForTeam(apiResult.favoriteTeamId)
                    }

                    setNavBarColorForTeam(
                        if (apiResult.useTeamColorNavBar) {
                            apiResult.favoriteTeamId
                        } else null
                    )

                    favoriteTeamColorPreference.isChecked = apiResult.useTeamColorNavBar

                    startingScreenPreference.value = apiResult.startingLocation

                } catch (ex: Exception) {
                    Log.i(
                        TAG,
                        """Settings not found.
                          |This may just mean they haven't been initialized yet.
                          |""".trimMargin()
                    )
                    saveSettings(userName)
                }
            }
        }
    }

    companion object {
        const val TAG = "SettingsFragment"

        const val aboutPreferenceCategoryKey = "aboutCategory"
        const val aboutTheAppPreferenceKey = "aboutTheApp"
        const val creditsPreferenceKey = "credits"
        const val favoriteTeamPreferenceKey = "favoriteTeam"
        const val favoriteTeamColorsPreferenceKey = "useFavoriteTeamColors"
        const val notificationsPreferenceKey = "notifications"
        const val startingScreenPreferenceKey = "startingScreen"
        const val usernamePreferenceKey = "username"
    }
}