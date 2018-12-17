package com.gmail.htaihm.colorchanger

import android.arch.lifecycle.ViewModel

class UserProfileViewModel : ViewModel() {

    val user = User(name = "", company = "")
}

data class User(var name : String, var company: String)