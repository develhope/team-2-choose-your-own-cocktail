package co.develhope.chooseyouowncocktail_g2

import co.develhope.chooseyouowncocktail_g2.model.domainmodel.drinks.Drink
import java.util.*


object DrinkList {

    private var drinks = listOf<Drink>()

    fun drinkList(): List<Drink> {
        return drinks
    }

    fun List<Drink>.setList() {
        drinks = this
    }
    
    fun setFavorite(Drink: Drink, bool: Boolean) {
        Collections.replaceAll(
            drinks,
            Drink,
            Drink.copy(favourite = bool)
        )
    }

}