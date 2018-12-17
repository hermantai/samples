package com.gmail.htaihm.colorchanger

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.util.*


/**
 * Demo viewmodel, live data.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var colorChangerViewModel: ColorChangerViewModel
    private var fruitIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainActivityRootView = findViewById<View>(R.id.root_view)
        val changeColorButton = findViewById<Button>(R.id.change_color_button)
        val display = findViewById<TextView>(R.id.display_text)
        val refreshFruits = findViewById<Button>(R.id.refresh_fruits_button)

        colorChangerViewModel = ViewModelProviders.of(this).get(ColorChangerViewModel::class.java)
        mainActivityRootView.setBackgroundColor(colorChangerViewModel.getColorResource())
        changeColorButton.setOnClickListener {
            val color = generateRandomColor()
            mainActivityRootView.setBackgroundColor(color)
            colorChangerViewModel.setColorResource(color)
        }

        val userViewModel = ViewModelProviders.of(this).get(UserProfileViewModel::class.java)
        userViewModel.user.name = "hello"

        val model = ViewModelProviders.of(this).get(FruitViewModel::class.java)
        model.getFruitList().observe(this, Observer<List<String>>{ fruitlist ->
            Log.i("MainActivity", "Got list of fruits {$fruitlist}")
            display.setText(fruitlist!!.get(fruitIndex))
            fruitIndex = (fruitIndex + 1) % fruitlist.size
        })

        refreshFruits.setOnClickListener {
            model.loadFruits()
        }
    }
    private fun generateRandomColor(): Int {
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }
}
