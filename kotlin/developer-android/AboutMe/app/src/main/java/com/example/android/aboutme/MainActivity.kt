package com.example.android.aboutme

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val doneButton: Button = findViewById(R.id.done_button)
    doneButton.setOnClickListener{ addNickname(it) }

    val nicknameText: TextView = findViewById(R.id.nickname_text)
    nicknameText.setOnClickListener{
      updateNickname(it)
    }
  }

  private fun addNickname(view: View) {
    val nicknameText: TextView = findViewById(R.id.nickname_text)
    val nicknameEdit: EditText = findViewById(R.id.nickname_edit)

    if (nicknameEdit.text.isEmpty()) {
      Snackbar.make(view, "The nickname cannot be empty", Snackbar.LENGTH_SHORT).show()
      return
    }

    // remove styling from EditText (e.g. underline for typos) when setting to nicknameText.text
    nicknameText.text = nicknameEdit.text.toString()
    nicknameEdit.visibility = View.GONE
    view.visibility = View.GONE
    nicknameText.visibility = View.VISIBLE

    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
  }

  private fun updateNickname(view: View) {
    val nicknameText: TextView = findViewById(R.id.nickname_text)
    val nicknameEdit: EditText = findViewById(R.id.nickname_edit)
    val doneButton: Button = findViewById(R.id.done_button)

    nicknameEdit.setText("")
    nicknameEdit.visibility = View.VISIBLE
    doneButton.visibility = View.VISIBLE
    nicknameText.visibility = View.GONE

    nicknameEdit.requestFocus()
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(nicknameEdit, 0)
  }
}