package com.example.android.aboutme

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.android.aboutme.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding
  private val initialMyName: MyName = MyName("Herman 2")

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    binding.myName = initialMyName

    binding.doneButton.setOnClickListener { addNickname() }

    binding.nicknameText.setOnClickListener {
      updateNickname()
    }
  }

  private fun addNickname() {
    binding.apply {
      if (nicknameEdit.text.isEmpty()) {
        Snackbar.make(doneButton, "The nickname cannot be empty", Snackbar.LENGTH_SHORT).show()
        return
      }

      // remove styling from EditText (e.g. underline for typos) when setting to nicknameText.text
      //   nicknameText.text = nicknameEdit.text.toString()
      myName?.nickname = nicknameEdit.text.toString()
      invalidateAll()

      nicknameEdit.visibility = View.GONE
      doneButton.visibility = View.GONE
      nicknameText.visibility = View.VISIBLE

      val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      inputMethodManager.hideSoftInputFromWindow(doneButton.windowToken, 0)
    }
  }

  private fun updateNickname() {
    binding.apply {
      nicknameEdit.setText("")
      nicknameEdit.visibility = View.VISIBLE
      doneButton.visibility = View.VISIBLE
      nicknameText.visibility = View.GONE

      nicknameEdit.requestFocus()
      val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      imm.showSoftInput(nicknameEdit, 0)
    }
  }
}