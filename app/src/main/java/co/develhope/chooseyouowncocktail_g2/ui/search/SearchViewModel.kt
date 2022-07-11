package co.develhope.chooseyouowncocktail_g2.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.develhope.chooseyouowncocktail_g2.DrinkList
import co.develhope.chooseyouowncocktail_g2.domain.model.Drink
import co.develhope.chooseyouowncocktail_g2.network.DrinksProvider
import co.develhope.chooseyouowncocktail_g2.ui.home.DBEvent
import co.develhope.chooseyouowncocktail_g2.ui.home.DBResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {


    private val dbProvider: DrinksProvider = DrinksProvider()

    private var _result = MutableLiveData<DBResult>()
    val result: LiveData<DBResult>
        get() = _result

    private var _list = MutableLiveData<List<Drink>>()
    val list: LiveData<List<Drink>>
        get() = _list


    fun send(event: DBEvent) =
        CoroutineScope(Dispatchers.Main).launch {
            when (event) {
                is DBEvent.RetrieveDrinksByFirstLetter -> retrieveDB(
                    dbProvider.searchByFirstLetter(
                        event.letter
                    )
                )
            }
        }

    private fun retrieveDB(list: List<Drink>) {
        Log.d("MainViewModel", "Retrieving from thecocktaildb.com")
        _result.value = DBResult.Loading
        try {
            if (DrinkList.letterIndex != DrinkList.currentLetter.size) DrinkList.letterIndex += 1
            _result.value = DBResult.Result(list)
        } catch (e: Exception) {
            _result.value =
                DBResult.Error("error retrieving from DB: ${e.localizedMessage}")
        }
    }

    fun filterList(list: List<Drink>, query: String): List<Drink> {
        return list.filter {
            it.name!!.contains(query, true) ||
                    it.description!!.contains(query, true) ||
                    it.shortDescription!!.contains(query, true)

        }
    }

}