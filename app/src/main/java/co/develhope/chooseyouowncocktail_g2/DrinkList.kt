package co.develhope.chooseyouowncocktail_g2

import co.develhope.chooseyouowncocktail_g2.domain.model.Drink
import java.util.*


object DrinkList {

    val currentLetter = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray()
    var letterIndex = 0

    private var drinkList = listOf<Drink>()
    private var originList = listOf<Drink>()


    fun drinkList(): List<Drink> {
        return drinkList
    }

    fun originDrinkList(): List<Drink> {
        return originList
    }


    fun List<Drink>.setList() {
        drinkList = this
    }

    fun getFavorite(): List<Drink> {
        return drinkList.filter { it.favourite }
    }

    fun addToDrinkList(drinks: List<Drink>) {
        val newList = drinkList().toMutableList()
        val newOrignList = drinkList().toMutableList()
        newList.addAll(drinks)
        newOrignList.addAll(drinks)
        originList = newOrignList
        drinkList = newList
    }

    fun getByID(id: Int): Drink? {
        return drinkList().firstOrNull { it.id == id }
    }

    fun setFavorite(drink: Drink, bool: Boolean) {
        Collections.replaceAll(
            drinkList,
            drink,
            drink.copy(favourite = bool)
        )
    }

}