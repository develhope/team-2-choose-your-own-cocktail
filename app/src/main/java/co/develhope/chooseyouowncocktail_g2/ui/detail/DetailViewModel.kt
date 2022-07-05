package co.develhope.chooseyouowncocktail_g2.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.develhope.chooseyouowncocktail_g2.DrinkList.drinkList
import co.develhope.chooseyouowncocktail_g2.domain.model.Drink


class DetailViewModel : ViewModel() {

    private var _drink = MutableLiveData<Drink>()
    val drink: LiveData<Drink>
        get() = _drink

    fun updateDetailedDrink(drink: Drink) {
        _drink.value = drinkList().first { it.id == drink.id }
    }


}