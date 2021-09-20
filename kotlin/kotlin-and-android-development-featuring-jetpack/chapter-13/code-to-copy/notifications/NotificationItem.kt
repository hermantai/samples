package dev.mfazio.abl.notifications

import android.content.Context
import kotlinx.coroutines.CoroutineScope

data class NotificationItem(
    val id: Int,
    val title: String,
    val description: String,
    val notificationType: NotificationType,
    val action: (context: Context, coroutineScope: CoroutineScope) -> Unit
) {
    fun getNotificationTypeShortName() = notificationType.name.take(1)
}

enum class NotificationType { Local, Push }