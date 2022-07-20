package co.develhope.chooseyouowncocktail_g2.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import co.develhope.chooseyouowncocktail_g2.DrinkList

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
import java.util.*

sealed class SearchResult {
    object Loading : SearchResult()
    data class Result(val result: List<Drink>) : SearchResult()
    object NullResult : SearchResult()
    data class Error(val message: String) : SearchResult()
}

class SearchViewModel(val drinkList: DrinkList) : ViewModel() {

    private val dbProvider: DrinksProvider = DrinksProvider()

    private var _result = MutableStateFlow<DBResult>(DBResult.Result)
    val result: StateFlow<DBResult>
        get() = _result
    private var _search = MutableStateFlow<SearchResult>(SearchResult.Result(listOf()))
    val search: StateFlow<SearchResult>
        get() = _search

    var isLoading = false

    var resultList = listOf<Drink>()


    fun send(event: DBEvent) =
        CoroutineScope(Dispatchers.Main).launch {
            when (event) {
                is DBEvent.RetrieveDrinksByFirstLetter -> retrieveDB(
                    dbProvider.searchByFirstLetter(
                        event.letter
                    )
                )
                is DBEvent.RetrieveDrinksByName -> searchOnDB(
                    dbProvider.searchByName(event.name)
                )
            }
        }

    fun increaseCurrentLetter() {
        if (checkCurrentLetter()) drinkList.letterIndex += 1
    }

    private fun checkCurrentLetter(): Boolean {
        return drinkList.letterIndex < drinkList.indexLetter.size
    }

    private fun retrieveDB(list: DrinksResult) {
        Log.d("MainViewModel", "Retrieving from thecocktaildb.com")
        _result.value = DBResult.Loading
        try {
            _result.value = DBResult.Result
            drinkList.addToDrinkList(DrinkMapper.listToDomainModel(list))
        } catch (e: Exception) {
            when (e) {
                is NullPointerException -> _result.value = DBResult.NullResult
                else -> _result.value =
                    DBResult.Error("error retrieving from DB: ${e.localizedMessage}")
            }
        }
    }

    private fun searchOnDB(list: DrinksResult) {
        Log.d("MainViewModel", "Searching in thecocktaildb.com")
        _search.value = SearchResult.Loading
        try {
            _search.value = SearchResult.Result(DrinkMapper.listToDomainModel(list))
        } catch (e: Exception) {
            when (e) {
                is NullPointerException -> _search.value = SearchResult.NullResult
                else -> _search.value =
                    SearchResult.Error("error retrieving from DB: ${e.localizedMessage}")
            }
        }
    }

    fun getByID(id: Int): Drink? {
        return drinkList.getList().firstOrNull { it.id == id }

    }


    fun moveItem(drink: Drink, destPos: Int) {
        val drinksList = drinkList.getList().toMutableList()
        drinkList.getList().forEach {
            if (it.id == drink.id) {
                drinksList.remove(it)
                drinksList.add(destPos, it)
            }

        }
        drinkList.setList(drinksList)
    }

    fun removeItem(drink: Drink) {
        val drinksList = drinkList.getList().toMutableList()
        drinkList.getList().forEach {

            if (it.id == drink.id) {
                drinksList.remove(it)
            }
        }

        drinkList.setList(drinksList)

    }

    fun addItem(drink: Drink) {
        val drinksList = drinkList.getList().toMutableList()
        drinksList.add(0, drink.copy(favourite = true))
        drinkList.setList(drinksList)
    }

    fun restoreOriginPos(drink: Drink): Int {
        var destPost = 0
        val drinkList = drinkList.getList().toMutableList()

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
        return drinkList.getList().let { list -> list.indexOf(list.find { it.id == drink.id }) }
    }

    fun setFavoriteOnSearchResult(
        drinkList: List<Drink>,
        drink: Drink,
        bool: Boolean
    ): List<Drink> {
        Collections.replaceAll(
            drinkList,
            drink,
            drink.copy(favourite = bool)
        )
        return drinkList
    }

    fun checkExistingFavorite(resultList: List<Drink>): List<Drink> {
        val mutableRestList = resultList.toMutableList()
        mutableRestList.forEach { drink ->
            drinkList.getList().forEach {
                if (it.id == drink.id) {
                    Collections.replaceAll(
                        mutableRestList,
                        drink,
                        drink.copy(favourite = it.favourite)
                    )
                }
            }
        }
        return mutableRestList

    }


    private fun getOriginPos(drink: Drink): Int {
        var oldPos = 0
        drinkList.originDrinkList().forEachIndexed { index, originDrink ->

            if (originDrink.id == drink.id) {
                oldPos = index
            }
        }
        return oldPos
    }

}