package com.gmail.htaihm.colorchanger

import android.arch.lifecycle.ViewModel

class ColorChangerViewModel : ViewModel() {
    private var colorResource: Int = 0xfff
    fun setColorResource(colorResource: Int) {
        this.colorResource = colorResource
    }
    fun getColorResource() = colorResource
}