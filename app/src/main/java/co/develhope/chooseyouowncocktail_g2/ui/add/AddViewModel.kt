package co.develhope.chooseyouowncocktail_g2.ui.add

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import co.develhope.chooseyouowncocktail_g2.DrinkList
import co.develhope.chooseyouowncocktail_g2.usecase.model.Drink
import com.google.gson.Gson

const val USER_DRINK_KEY = "user_drinks"

class AddViewModel(val preferences: SharedPreferences, val drinkList: DrinkList) : ViewModel() {

    fun saveLocalDrink(drink: Drink) {
        drinkList.addToUserDrinks(drink)
  drinkList.getUserDrinks().forEach { println(it.name) }
        preferences.edit().putString(
            USER_DRINK_KEY,
            Gson().toJson(drinkList.getLocalUserDrinks())
        ).apply()
    }


}