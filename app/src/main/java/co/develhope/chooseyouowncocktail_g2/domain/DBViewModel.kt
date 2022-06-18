package co.develhope.chooseyouowncocktail_g2.domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.develhope.chooseyouowncocktail_g2.DrinkList
import co.develhope.chooseyouowncocktail_g2.network.DrinksProvider
import co.develhope.chooseyouowncocktail_g2.domain.model.Drink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class DBEvent {
    data class RetrieveDrinksByFirstLetter(val letter: Char) : DBEvent()
}

sealed class DBResult {
    object Loading : DBResult()
    data class Result(val db: List<Drink>) : DBResult()
    data class Error(val message: String) : DBResult()
}

class DBViewModel : ViewModel() {

    private val DBProvider: DrinksProvider = DrinksProvider()

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
        _result.value=DBResult.Loading
        try {
            println("retrieve")
            if (DrinkList.letterIndex != DrinkList.currentLetter.size) DrinkList.letterIndex += 1
            _result.value = DBResult.Result(list)
        } catch (e: Exception) {
            _result.value =
                DBResult.Error("error retrieving from DB: ${e.localizedMessage}")
        }
    }
}