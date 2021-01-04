package com.android.example.colormyviews

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
  private lateinit var boxThreeText: TextView
  private lateinit var boxFourText: TextView
  private lateinit var boxFiveText: TextView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    boxThreeText = findViewById<TextView>(R.id.box_three_text)
    boxFourText = findViewById<TextView>(R.id.box_four_text)
    boxFiveText = findViewById<TextView>(R.id.box_five_text)

    setListeners()
  }

  private fun makeColored(view: View) {
    when (view.id) {
      R.id.box_one_text -> view.setBackgroundColor(Color.DKGRAY)
      R.id.box_two_text -> view.setBackgroundColor(Color.GRAY)
      R.id.box_three_text -> view.setBackgroundColor(Color.BLUE)
      R.id.box_four_text -> view.setBackgroundColor(Color.MAGENTA)
      R.id.box_five_text -> view.setBackgroundColor(Color.BLUE)
      R.id.red_button ->boxThreeText.setBackgroundResource(R.color.my_red)
      R.id.yellow_button ->boxFourText.setBackgroundResource(R.color.my_yellow)
      R.id.green_button ->boxFiveText.setBackgroundResource(R.color.my_green)
      else -> view.setBackgroundColor(Color.LTGRAY)
    }
  }

  private fun setListeners() {

    val boxOneText = findViewById<TextView>(R.id.box_one_text)
    val boxTwoText = findViewById<TextView>(R.id.box_two_text)

    val rootConstraintLayout = findViewById<View>(R.id.constraint_layout)

    val redButton = findViewById<Button>(R.id.red_button)
    val greenButton = findViewById<Button>(R.id.green_button)
    val yellowButton = findViewById<Button>(R.id.yellow_button)

    val clickableViews: List<View> = listOf(
      boxOneText,
      boxTwoText,
      boxThreeText,
      boxFourText,
      boxFiveText,
      rootConstraintLayout,
      redButton,
      greenButton,
      yellowButton
    )
    for (item in clickableViews) {
      item.setOnClickListener { makeColored(it) }
    }
  }

}