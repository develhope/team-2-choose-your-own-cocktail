package co.develhope.chooseyouowncocktail_g2

import android.util.Log
import co.develhope.chooseyouowncocktail_g2.domain.model.Drink
import java.util.*


object DrinkList {

    private var drinks = listOf<Drink>()




    fun drinkList(): List<Drink> {
        return drinks
    }


    fun List<Drink>.setList() {
        drinks = this
    }


    fun getByID(id: Int): Drink? {
        return drinkList().firstOrNull() { it.id == id }
    }

    fun setSaves(drink: Drink, bool: Boolean) {
         Collections.replaceAll(
            drinks,
            drink,
            drink.copy(favourite = bool)
        )
        drinks.forEach { Log.d("debugSaves", "${it.name} and ${it.favourite} and id is ${it.id}")}
    }

    fun returnOnlyPreferiteSelectedDrink(): List<Drink> {
        drinks.forEach { Log.d("debugONly Saves", "${it.name} and ${it.favourite}")}
        return drinks.filter { it.favourite == true }
    }


}