package co.develhope.chooseyouowncocktail_g2.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import co.develhope.chooseyouowncocktail_g2.DrinkList
import co.develhope.chooseyouowncocktail_g2.DrinkList.drinkList
import co.develhope.chooseyouowncocktail_g2.DrinkList.originDrinkList
import co.develhope.chooseyouowncocktail_g2.DrinkList.setList
import co.develhope.chooseyouowncocktail_g2.usecase.DrinkMapper
import co.develhope.chooseyouowncocktail_g2.network.DrinksProvider
import co.develhope.chooseyouowncocktail_g2.usecase.model.Drink
import co.develhope.chooseyouowncocktail_g2.network.dto.DrinksResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DBEvent {
    data class RetrieveDrinksByFirstLetter(val letter: Char) : DBEvent()
}

sealed class DBResult {
    object Loading : DBResult()
    data class Result(val db: List<Drink>) : DBResult()
    object NullResult : DBResult()
    data class Error(val message: String) : DBResult()
}

class HomeViewModel : ViewModel() {

    private val dbProvider: DrinksProvider = DrinksProvider()

    private var _result = MutableStateFlow<DBResult>(DBResult.Result(emptyList()))
    val result: StateFlow<DBResult>
        get() = _result

    private var _list = MutableStateFlow<List<Drink>>(emptyList())
    val list: StateFlow<List<Drink>>
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

    private fun retrieveDB(list: DrinksResult) {
        Log.d("MainViewModel", "Retrieving from thecocktaildb.com")
        _result.value = DBResult.Loading
        try {
            val retrievedDrinks = DrinkMapper.listToDomainModel(list)
            _result.value = DBResult.Result(
                retrievedDrinks
            )
            DrinkList.addToDrinkList(retrievedDrinks)
        } catch (e: Exception) {
            when (e) {
                is NullPointerException -> _result.value = DBResult.NullResult
                else -> _result.value =
                    DBResult.Error("error retrieving from DB: ${e.localizedMessage}")
            }

        }
    }

    fun increaseCurrentLetter() {
        if (checkCurrentLetter()) DrinkList.letterIndex += 1
    }

    fun checkCurrentLetter(): Boolean {
        return DrinkList.letterIndex < DrinkList.indexLetter.size
    }


    fun moveItem(drink: Drink, destPos: Int) {
        val drinksList = drinkList().toMutableList()
        drinkList().forEach {
            if (it.id == drink.id) {
                drinksList.remove(it)
                drinksList.add(destPos, it)
            }
        }
        drinksList.setList()
        _list.value = drinksList
    }

    fun restoreOriginPos(drink: Drink): Int {
        var destPost = 0
        val drinkList = drinkList().toMutableList()
        val sorteredList = drinkList.filterNot { it.favourite }.sortedBy { getOriginPos(it) }
        drinkList.sortedBy { sorteredList.indexOf(it) }
            .forEachIndexed { index, originDrink ->
                if (originDrink.id == drink.id) {
                    moveItem(originDrink, index)
                    destPost = 0
                }
            }
        return destPost
    }

    fun getFromPos(drink: Drink): Int {
        return drinkList().let { list -> list.indexOf(list.find { it.id == drink.id }) }
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


}