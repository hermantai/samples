package com.gmail.htaihm.playdatabinding

import android.databinding.BindingAdapter
import android.widget.TextView

@BindingAdapter("matchesText")
fun matchesText(textView: TextView, matches: Int) {
    val resources = textView.context.resources
    textView.text = resources.getQuantityString(R.plurals.matches, matches, matches)
}