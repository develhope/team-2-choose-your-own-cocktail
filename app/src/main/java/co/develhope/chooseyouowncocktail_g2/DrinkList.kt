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

    fun setPrefer(drink: Drink, bool: Boolean) {

        for (drinkthis in drinks){
            if (drinkthis.name == drink.name){
                drinkthis.favourite = bool
            }
        }
    }

    fun returnOnlyPreferiteSelectedDrink(): List<Drink> {
        return drinks.filter { it.favourite == true }
    }


}