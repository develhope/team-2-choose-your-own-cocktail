package co.develhope.chooseyouowncocktail_g2

import co.develhope.chooseyouowncocktail_g2.usecase.model.Drink
import java.util.*


class DrinkList {

    val indexLetter = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray()
    var letterIndex = 0

    private val drinkList = mutableListOf<Drink>()
    private val originList = mutableListOf<Drink>()


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