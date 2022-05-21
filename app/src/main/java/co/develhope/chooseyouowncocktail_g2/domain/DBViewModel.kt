package co.develhope.chooseyouowncocktail_g2.domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.develhope.chooseyouowncocktail_g2.DrinksProvider
import co.develhope.chooseyouowncocktail_g2.model.domainmodel.drinks.Drink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class DBEvent {
    data class RetrieveDrinksByFirstLetter(val letter: Char) : DBEvent()
}

sealed class DBResult {
    data class Result(val db: List<Drink>) : DBResult()
    data class Error(val message: String) : DBResult()
}

class DBViewModel(private val DBProvider: DrinksProvider) : ViewModel() {


    private var _result = MutableLiveData<DBResult>()
    val result: LiveData<DBResult>
        get() = _result


    fun send(event: DBEvent) =
        CoroutineScope(Dispatchers.Main).launch {
            when (event) {
                is DBEvent.RetrieveDrinksByFirstLetter -> retrieveDB(
                    DBProvider.searchByFirstLetter(
                        event.letter
                    )
                )
            }
        }

    private fun retrieveDB(list: List<Drink>) {
        Log.d("MainViewModel", "Retrieving from thecocktaildb.com")
        try {
            _result.value = DBResult.Result(list)
        } catch (e: Exception) {
            _result.value =
                DBResult.Error("error retrieving from DB: ${e.localizedMessage}")
        }
    }
}