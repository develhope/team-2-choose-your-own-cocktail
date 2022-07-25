package co.develhope.chooseyouowncocktail_g2.ui.home

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import co.develhope.chooseyouowncocktail_g2.DrinkList
import co.develhope.chooseyouowncocktail_g2.network.DrinksProvider
import co.develhope.chooseyouowncocktail_g2.network.dto.DrinksResult
import co.develhope.chooseyouowncocktail_g2.usecase.DrinkMapper
import co.develhope.chooseyouowncocktail_g2.usecase.model.Drink
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


const val FAVORITE_KEY = "favorite"

sealed class DBEvent {
    data class RetrieveDrinksByFirstLetter(val letter: Char) : DBEvent()
    data class RetrieveDrinksByName(val name: String) : DBEvent()
}

sealed class DBResult {
    object Loading : DBResult()
    object Result : DBResult()
    object NullResult : DBResult()
    data class Error(val message: String) : DBResult()
}

class HomeViewModel(val drinkList: DrinkList, val preferences: SharedPreferences) : ViewModel() {

    private val dbProvider: DrinksProvider = DrinksProvider()

    private var _result = MutableStateFlow<DBResult>(DBResult.Result)
    val result: StateFlow<DBResult>
        get() = _result

    private var _list = MutableStateFlow<List<Drink>>(emptyList())
    val list: StateFlow<List<Drink>>
        get() = _list

    var isLoading = false

    init {
        _list.value = drinkList.getList()
    }


    fun send(event: DBEvent) =
        CoroutineScope(Dispatchers.Main).launch {
            when (event) {
                is DBEvent.RetrieveDrinksByFirstLetter -> retrieveDB(
                    dbProvider.searchByFirstLetter(
                        event.letter
                    )
                )
                is DBEvent.RetrieveDrinksByName -> retrieveDB(
                    dbProvider.searchByName(event.name)
                )
            }
        }

    private fun retrieveDB(list: DrinksResult) {
        Log.d("MainViewModel", "Retrieving from thecocktaildb.com")
        _result.value = DBResult.Loading
        try {
            val retrievedDrinks = DrinkMapper.listToDomainModel(list)
            _result.value = DBResult.Result
            drinkList.addToDrinkList(retrievedDrinks)
            if (preferences.contains(FAVORITE_KEY)) {
                combineStoredFavWithLocalList()
            }
        } catch (e: Exception) {
            when (e) {
                is NullPointerException -> _result.value = DBResult.NullResult
                else -> _result.value =
                    DBResult.Error("error retrieving from DB: ${e.localizedMessage}")
            }

        }
    }

    fun increaseCurrentLetter() {
        if (checkCurrentLetter()) drinkList.letterIndex += 1
    }

    fun checkCurrentLetter(): Boolean {
        return drinkList.letterIndex < drinkList.indexLetter.size
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
        _list.value = drinksList
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

    fun removeItem(drink: Drink) {
        val drinksList = drinkList.getList().toMutableList()
        drinkList.getList().forEach {
            if (it.id == drink.id) {
                drinksList.remove(it)
            }
        }
        drinkList.setList(drinksList)
    }

    fun getFromPos(drink: Drink): Int {
        return drinkList.getList().let { list -> list.indexOf(list.find { it.id == drink.id }) }
    }

    fun setFavorite(drink: Drink, bool: Boolean) {
        drinkList.setFavorite(drink, bool)
        preferences.edit().putString(
            FAVORITE_KEY,
            Gson().toJson(drinkList.getFavorite())
        ).apply()
    }

    private fun getFavoriteFromSharedPreferences(): List<Drink?> {
        return Gson().fromJson(
            preferences.getString(FAVORITE_KEY, null),
            object : TypeToken<List<Drink?>?>() {}.type
        )
    }

    private fun combineStoredFavWithLocalList() {
        val storedFav = getFavoriteFromSharedPreferences().asReversed()
        storedFav.forEach { drink ->
            drinkList.getList().find { it.id == drink?.id }.apply {
                if (this != null) {
                    removeItem(this)
                }
            }
        }
        drinkList.setStoredFavorite(storedFav as List<Drink>)
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