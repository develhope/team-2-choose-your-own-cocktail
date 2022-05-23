package co.develhope.chooseyouowncocktail_g2.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.develhope.chooseyouowncocktail_g2.domain.model.Drink

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _result = MutableLiveData<Drink>()
    val result: LiveData<Drink> = _result


}