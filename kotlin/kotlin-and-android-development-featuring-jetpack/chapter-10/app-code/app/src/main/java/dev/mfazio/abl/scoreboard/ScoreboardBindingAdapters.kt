package dev.mfazio.abl.scoreboard

import android.media.Image
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import dev.mfazio.abl.R

@BindingAdapter("isOccupied")
fun bindIsOccupied(imageView: ImageView, isOccupied: Boolean) {
    imageView.setImageResource(
        if(isOccupied) R.drawable.scoreboard_status_base_occupied
        else R.drawable.scoreboard_status_base_empty
    )
}

@BindingAdapter("isVisible")
fun bindIsVisible(view: View, isVisible: Boolean) {
    view.visibility = if(isVisible) View.VISIBLE else View.GONE
}