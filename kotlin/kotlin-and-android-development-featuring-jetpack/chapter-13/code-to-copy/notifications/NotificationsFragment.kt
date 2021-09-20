package dev.mfazio.abl.notifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dev.mfazio.abl.api.services.getDefaultABLService
import dev.mfazio.abl.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNotificationsBinding.inflate(inflater)

        val adapter = NotificationsAdapter().apply {
            submitList(notificationItems)
        }

        with(binding.notificationsList) {
            this.adapter = adapter
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }

        return binding.root
    }

    companion object {
        private val ablService = getDefaultABLService()       

        private val notificationItems = listOf<NotificationItem>()
    }
}