package dev.mfazio.abl.notifications

data class NotificationConfig(
    val id: Int,
    val channel: ABLNotificationChannel,
    val titleInput: String,
    val textInput: String,
    val smallIconId: Int,
    val destinationId: Int,
    val arguments: Map<String, String>
)
