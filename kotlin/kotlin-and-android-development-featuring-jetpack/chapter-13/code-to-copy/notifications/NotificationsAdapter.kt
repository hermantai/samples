package dev.mfazio.abl.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.mfazio.abl.R
import dev.mfazio.abl.databinding.NotificationItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class NotificationsAdapter :
    ListAdapter<NotificationItem, NotificationsAdapter.ViewHolder>(NotificationItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.notification_item,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: NotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notificationItem: NotificationItem) {
            binding.notificationItem = notificationItem
            binding.clickListener = View.OnClickListener {
                notificationItem.action(it.context, CoroutineScope(Dispatchers.Default))
            }
        }
    }
}

private class NotificationItemDiffCallback : DiffUtil.ItemCallback<NotificationItem>() {
    override fun areItemsTheSame(oldItem: NotificationItem, newItem: NotificationItem) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: NotificationItem, newItem: NotificationItem) =
        oldItem == newItem
}