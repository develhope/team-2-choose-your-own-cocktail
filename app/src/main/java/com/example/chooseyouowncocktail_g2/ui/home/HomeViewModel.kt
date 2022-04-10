package com.example.chooseyouowncocktail_g2.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "touch me to see detail page"
    }
    val text: LiveData<String> = _text
}