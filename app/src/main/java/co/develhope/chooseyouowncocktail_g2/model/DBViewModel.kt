package co.develhope.chooseyouowncocktail_g2.model

import DrinkResultModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.develhope.chooseyouowncocktail_g2.DrinksDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class DBEvent {
    object RetrieveDrinksAlphabetically : DBEvent()
    object RetrieveDrinksRandom : DBEvent()
}

sealed class DBResult {
    data class Result(val db: List<DrinkResultModel>) : DBResult()
    data class Error(val message: String) : DBResult()
}

class DBViewModel(private val DBProvider: DrinksDB) : ViewModel() {


    private var _result = MutableLiveData<DBResult>()
    val result: LiveData<DBResult>
        get() = _result


    fun send(event: DBEvent) =
        CoroutineScope(Dispatchers.Main).launch {
            when (event) {
                is DBEvent.RetrieveDrinksAlphabetically -> retrieveDB(DBProvider.getListAlphabetically())
                is DBEvent.RetrieveDrinksRandom -> retrieveDB(DBProvider.getRandom())
            }
        }

    private fun retrieveDB(list: List<DrinkResultModel>) {
        Log.d("MainViewModel", "Retrieving from thecocktaildb.com")
        try {
            _result.value = DBResult.Result(list)
        } catch (e: Exception) {
            _result.value =
                DBResult.Error("error retrieving from DB: ${e.localizedMessage}")
        }
    }
}