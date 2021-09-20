package dev.mfazio.abl.notifications

import android.app.NotificationManager

enum class ABLNotificationChannel(
    val channelId: String,
    val channelName: String,
    val channelDescription: String = "",
    val importance: Int = NotificationManager.IMPORTANCE_DEFAULT
) {
    Default(
        "ablNotificationsDefault",
        "Default",
        importance = NotificationManager.IMPORTANCE_DEFAULT
    ),
    Players(
        "ablNotificationsPlayers",
        "Players",
        "Player-specific notifications",
        NotificationManager.IMPORTANCE_HIGH
    ),
    Teams(
        "ablNotificationsTeams",
        "Teams",
        "Team-specific notifications",
        NotificationManager.IMPORTANCE_LOW
    ),
    ScoreUpdates(
        "ablNotificationsScoreUpdates",
        "Score Updates",
        "ScoreUpdates",
        NotificationManager.IMPORTANCE_MAX
    )
}