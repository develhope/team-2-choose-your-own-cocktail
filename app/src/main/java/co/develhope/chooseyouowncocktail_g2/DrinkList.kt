package co.develhope.chooseyouowncocktail_g2

import android.util.Log
import co.develhope.chooseyouowncocktail_g2.domain.model.Drink
import java.util.*
import kotlin.collections.ArrayList


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

    fun setFavorite(drink: Drink, bool: Boolean) {
        Collections.replaceAll(
            drinks,
            drink,
            drink.copy(favourite = bool)
        )

    }

    fun booleanSortDrinkList(){
        drinks = this.drinks.sortedBy { it.favourite }
        drinks.forEach{ Log.d("debug", "${it.name} and ${it.favourite}") }

    }

    fun returnOnlyPreferiteSelectedDrink(): List<Drink> {
        return drinks.filter { it.favourite == true }
    }

    fun repleacePreferiteOnSelfTop(preferiteList: List<Drink>){
        drinks = drinks.toMutableList()
        for(prefDrink in preferiteList){
            (drinks as MutableList<Drink>).remove(prefDrink)
        }
        drinks.forEach { Log.d("debug2", "${it.name} and ${it.favourite}")}

        val tmpDrinkList = ArrayList<Drink>()
        tmpDrinkList.addAll(preferiteList)
        tmpDrinkList.addAll(drinks)
        drinks = tmpDrinkList
        //drinks.forEach { Log.d("debug refac list", "${it.name} and ${it.favourite}")}
    }


}