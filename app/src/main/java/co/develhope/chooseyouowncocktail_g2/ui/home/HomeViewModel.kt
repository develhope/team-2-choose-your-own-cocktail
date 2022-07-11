package co.develhope.chooseyouowncocktail_g2.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import co.develhope.chooseyouowncocktail_g2.DrinkList
import co.develhope.chooseyouowncocktail_g2.DrinkList.drinkList
import co.develhope.chooseyouowncocktail_g2.DrinkList.originDrinkList
import co.develhope.chooseyouowncocktail_g2.DrinkList.setList
import co.develhope.chooseyouowncocktail_g2.domain.model.Drink
import co.develhope.chooseyouowncocktail_g2.network.DrinksProvider
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

class HomeViewModel : ViewModel() {

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

    fun moveItem(drink: Drink, destPos: Int) {
        val drinksList = drinkList().toMutableList()
        drinkList().forEach {
            if (it.id == drink.id) {
                drinksList.remove(it)
                drinksList.add(destPos, it)
                drinksList.setList()
            }
        }
        _list.value = drinkList()
    }

    fun restoreOriginPos(drink: Drink) : Int{
        var destPost = 0
        val drinkList = drinkList().toMutableList()
        val sorteredList = drinkList.filterNot { it.favourite }.sortedBy { getOriginPos(it) }
        drinkList.sortedBy { sorteredList.indexOf(it) }
            .forEachIndexed { index, originDrink ->
                if (originDrink.id == drink.id) {
                    moveItem(drink, index)
                    destPost=0
                }
            }
        return destPost
    }


    private fun getOriginPos(drink: Drink): Int {
        var oldPos = 0
        originDrinkList().forEachIndexed { index, originDrink ->
            if (originDrink.id == drink.id) {
                oldPos = index
            }
        }
        return oldPos
    }

    fun getByID(id: Int): Drink? {
        return drinkList().firstOrNull { it.id == id }
    }

    fun getFromPos(drink: Drink): Int {
        return drinkList().let { list -> list.indexOf(list.find { it.id == drink.id }) }
    }


}