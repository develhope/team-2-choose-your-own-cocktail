package co.develhope.chooseyouowncocktail_g2.ui.home

import DrinkResultModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import co.develhope.chooseyouowncocktail_g2.DrinksDB
import co.develhope.chooseyouowncocktail_g2.DrinksDB.setList
import co.develhope.chooseyouowncocktail_g2.mapper.DrinkMapper
import co.develhope.chooseyouowncocktail_g2.network.DrinkDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _result = MutableLiveData<DrinkResultModel>()
    val result: LiveData<DrinkResultModel> = _result


}