package dev.mfazio.abl.settings

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.mfazio.abl.api.models.AppSettingsApiModel
import dev.mfazio.abl.api.services.getDefaultABLService

class SaveSettingsWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result = try {
        val userName = inputData.getString(userNameKey)

        if (userName != null) {
            Log.i(TAG, "Saving user settings for $userName")
            getDefaultABLService().saveAppSettings(
                AppSettingsApiModel(
                    userName,
                    inputData.getString(favoriteTeamKey) ?: "",
                    inputData.getBoolean(favoriteTeamColorCheckKey, false),
                    inputData.getString(startingScreenKey) ?: ""
                )
            )
        }

        Result.success()

    } catch (ex: Exception) {
        Log.e(TAG, "Exception saving settings to API")
        Result.failure()
    }

    companion object {
        const val TAG = "SaveSettingsWorker"
        const val userNameKey = "userName"
        const val favoriteTeamKey = "favoriteTeam"
        const val favoriteTeamColorCheckKey = "favoriteTeamColorCheck"
        const val startingScreenKey = "startingScreenKey"
    }
}