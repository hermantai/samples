package dev.mfazio.abl.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.mfazio.abl.R
import dev.mfazio.abl.databinding.ImageCreditItemBinding

class ImageCreditsAdapter :
    ListAdapter<ImageCredit, ImageCreditsAdapter.ViewHolder>(ImageCreditDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.image_credit_item,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ImageCreditItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageCredit: ImageCredit) {
            binding.imageCredit = imageCredit
        }
    }
}

private class ImageCreditDiffCallback : DiffUtil.ItemCallback<ImageCredit>() {
    override fun areItemsTheSame(oldItem: ImageCredit, newItem: ImageCredit) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ImageCredit, newItem: ImageCredit) =
        oldItem == newItem

}