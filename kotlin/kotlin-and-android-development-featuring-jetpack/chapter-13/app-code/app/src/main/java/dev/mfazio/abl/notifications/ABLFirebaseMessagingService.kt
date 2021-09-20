package dev.mfazio.abl.notifications

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev.mfazio.abl.R

class ABLFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d(TAG, "Obtained new Firebase Token: [$token]")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        baseContext?.let { ctx ->
            createNotificationConfig(ctx, message)?.let { config ->
                val pendingIntent = NavDeepLinkBuilder(ctx)
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(config.destinationId)
                    .setArguments(Bundle().apply {
                        config.arguments.forEach { (key, value) ->
                            putString(key, value)
                        }
                    }).createPendingIntent()

                val notification =
                    NotificationCompat.Builder(ctx, config.channel.channelId)
                        .setContentTitle(
                            ctx.getString(
                                R.string.generic_push_title,
                                config.titleInput
                            )
                        )
                        .setContentText(
                            ctx.getString(
                                R.string.generic_push_description,
                                config.textInput
                            )
                        )
                        .setStyle(
                            NotificationCompat.BigTextStyle().bigText(
                                ctx.getString(
                                    R.string.generic_push_description,
                                    config.textInput
                                )
                            )
                        )
                        .setSmallIcon(config.smallIconId)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build()

                NotificationManagerCompat.from(ctx).notify(config.id, notification)
            }
        }
    }

    private fun createNotificationConfig(
        ctx: Context,
        message: RemoteMessage
    ): NotificationConfig? =
        when (message.data["destination"]) {
            ctx.getString(R.string.notification_destination_player) ->
                NotificationConfig(
                    id = 10,
                    channel = ABLNotificationChannel.Players,
                    titleInput = message.data["playerName"] ?: "N/A",
                    textInput = message.data["playerName"] ?: "N/A",
                    smallIconId = R.drawable.ic_baseline_directions_run_24,
                    destinationId = R.id.singlePlayerFragment,
                    arguments = mapOf(
                        "playerId" to (message.data["playerId"] ?: ""),
                        "playerName" to (message.data["playerName"] ?: "")
                    )
                )
            ctx.getString(R.string.notification_destination_team) ->
                NotificationConfig(
                    id = 11,
                    channel = ABLNotificationChannel.Teams,
                    titleInput = message.data["teamName"] ?: "N/A",
                    textInput = message.data["teamName"] ?: "N/A",
                    smallIconId = R.drawable.ic_baseline_outlined_flag_24,
                    destinationId = R.id.singleTeamFragment,
                    arguments = mapOf(
                        "teamId" to (message.data["teamId"] ?: ""),
                        "teamName" to (message.data["teamName"] ?: "")
                    )
                )
            else -> null
        }

    companion object {
        private const val TAG = "ABLFirebaseMessagingService"
    }
}