package co.develhope.chooseyouowncocktail_g2

import android.content.SharedPreferences
import co.develhope.chooseyouowncocktail_g2.ui.add.USER_DRINK_KEY
import co.develhope.chooseyouowncocktail_g2.usecase.model.Drink
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


sealed class ListToShow {
    object AllDrinks : ListToShow()
    object FavoriteDrinks : ListToShow()
    object MyDrinks : ListToShow()
}

class DrinkList(val preferences: SharedPreferences) {

    val indexLetter = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray()
    var letterIndex = 0

    private val drinkList = mutableListOf<Drink>()
    private var userDrinkList = mutableListOf<Drink>()
    private val originList = mutableListOf<Drink>()

    init {
        userDrinkList = getUserDrinks()
    }


    fun getList(): List<Drink> {
        return drinkList
    }

    fun originDrinkList(): List<Drink> {
        return originList
    }


    fun setList(newList: MutableList<Drink>) {
        drinkList.clear()
        drinkList.addAll(newList)
    }

    fun getLocalUserDrinks(): List<Drink> {
        return userDrinkList
    }

    fun getUserDrinks(): MutableList<Drink> {
        return if (preferences.contains(USER_DRINK_KEY)) {
            Gson().fromJson(
                preferences.getString(USER_DRINK_KEY, null),
                object : TypeToken<List<Drink>>() {}.type
            )
        } else {
            mutableListOf()
        }
    }

    fun getFavorite(): List<Drink> {
        return drinkList.filter { it.favourite }
    }

    fun setStoredFavorite(drinks: List<Drink>) {
        drinkList.addAll(0, drinks)
    }

    fun addToDrinkList(drinks: List<Drink>) {
        drinkList.addAll(drinks)
        originList.addAll(drinks)
    }

    fun addToUserDrinks(drink: Drink) {
        userDrinkList.add(drink)
    }

    fun setFavorite(drink: Drink, isFavorite: Boolean) {
        if (originList.any { it.id == drink.id }) {
            Collections.replaceAll(
                drinkList,
                drink,
                drink.copy(favourite = isFavorite)
            )
        } else {
            if (isFavorite) {
                drinkList.add(0, drink.copy(favourite = isFavorite))
            } else {
                drinkList.remove(drink)
            }
        }
    }

}