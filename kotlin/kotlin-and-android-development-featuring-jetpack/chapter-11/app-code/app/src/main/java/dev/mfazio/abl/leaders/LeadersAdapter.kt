package dev.mfazio.abl.leaders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.mfazio.abl.NavGraphDirections
import dev.mfazio.abl.R
import dev.mfazio.abl.databinding.LeaderListItemBinding

class LeadersAdapter :
    ListAdapter<LeaderListItem, LeadersAdapter.ViewHolder>(LeadersDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.leader_list_item,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: LeaderListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(leaderListItem: LeaderListItem) {
            binding.leader = leaderListItem
            binding.clickListener = View.OnClickListener { view ->
                val action = NavGraphDirections.actionGoToPlayer(
                    leaderListItem.player.playerId,
                    leaderListItem.player.fullName
                )
                view.findNavController().navigate(action)
            }
        }
    }
}

private class LeadersDiffCallback : DiffUtil.ItemCallback<LeaderListItem>() {
    override fun areItemsTheSame(oldItem: LeaderListItem, newItem: LeaderListItem): Boolean =
        oldItem.itemId == newItem.itemId

    override fun areContentsTheSame(oldItem: LeaderListItem, newItem: LeaderListItem): Boolean =
        oldItem == newItem
}