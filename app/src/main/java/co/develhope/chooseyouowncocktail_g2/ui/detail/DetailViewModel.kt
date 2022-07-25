package co.develhope.chooseyouowncocktail_g2.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.develhope.chooseyouowncocktail_g2.DrinkList
import co.develhope.chooseyouowncocktail_g2.usecase.model.Drink


class DetailViewModel(val drinkList: DrinkList) : ViewModel() {

    private var _drink = MutableLiveData<Drink>()
    val drink: LiveData<Drink>
        get() = _drink

    fun updateDetailedDrink(drink: Drink) {
        val currentDrink = drinkList.getList().firstOrNull { it.id == drink.id }
        if (currentDrink != null) {
            _drink.value = currentDrink
        } else {
            _drink.value = drink.copy(favourite = !drink.favourite)
        }
    }

    fun initDetailPage(drink: Drink) {
        _drink.value = drink
    }


}