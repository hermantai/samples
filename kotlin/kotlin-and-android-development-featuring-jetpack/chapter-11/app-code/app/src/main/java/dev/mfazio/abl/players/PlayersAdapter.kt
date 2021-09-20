package dev.mfazio.abl.players

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.mfazio.abl.NavGraphDirections
import dev.mfazio.abl.R
import dev.mfazio.abl.databinding.PlayerListItemBinding

class PlayersAdapter :
    PagingDataAdapter<PlayerListItem, PlayersAdapter.ViewHolder>(PlayerListItemDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.player_list_item,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: PlayerListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(playerListItem: PlayerListItem?) {
            if (playerListItem != null) {
                binding.playerListItem = playerListItem
                binding.clickListener = View.OnClickListener { view ->
                    val action =
                        NavGraphDirections.actionGoToPlayer(
                            playerListItem.playerId,
                            playerListItem.playerName
                        )
                    view.findNavController().navigate(action)
                }
            }
        }
    }
}

private class PlayerListItemDiffCallback : DiffUtil.ItemCallback<PlayerListItem>() {
    override fun areItemsTheSame(oldItem: PlayerListItem, newItem: PlayerListItem) =
        oldItem.playerId == newItem.playerId

    override fun areContentsTheSame(oldItem: PlayerListItem, newItem: PlayerListItem) =
        oldItem == newItem
}
