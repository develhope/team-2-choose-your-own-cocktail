package co.develhope.chooseyouowncocktail_g2.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import co.develhope.chooseyouowncocktail_g2.DrinkList
import co.develhope.chooseyouowncocktail_g2.DrinkList.drinkList
import co.develhope.chooseyouowncocktail_g2.DrinkList.setList
import co.develhope.chooseyouowncocktail_g2.usecase.DrinkMapper
import co.develhope.chooseyouowncocktail_g2.usecase.model.Drink
import co.develhope.chooseyouowncocktail_g2.network.DrinksProvider
import co.develhope.chooseyouowncocktail_g2.network.dto.DrinksResult
import co.develhope.chooseyouowncocktail_g2.ui.home.DBEvent
import co.develhope.chooseyouowncocktail_g2.ui.home.DBResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val dbProvider: DrinksProvider = DrinksProvider()

    private var _result = MutableStateFlow<DBResult>(DBResult.Result(emptyList()))
    val result: StateFlow<DBResult>
        get() = _result

    var drinkList = DrinkList.getFavorite().ifEmpty { drinkList() }

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


    fun increaseCurrentLetter() {
        if (checkCurrentLetter()) DrinkList.letterIndex += 1
    }

    private fun checkCurrentLetter(): Boolean {
        return DrinkList.letterIndex < DrinkList.indexLetter.size
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

    fun getByID(id: Int): Drink? {
        return drinkList().firstOrNull { it.id == id }
    }

    fun filterList(list: List<Drink>, query: String): List<Drink> {
        return list.filter {
            it.name!!.contains(query, true) ||
                    it.description!!.contains(query, true) ||
                    it.shortDescription!!.contains(query, true)

        }
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
        drinkList = drinksList
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
        DrinkList.originDrinkList().forEachIndexed { index, originDrink ->
            if (originDrink.id == drink.id) {
                oldPos = index
            }
        }
        return oldPos
    }

}