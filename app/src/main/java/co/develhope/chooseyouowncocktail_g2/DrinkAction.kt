package co.develhope.chooseyouowncocktail_g2

import co.develhope.chooseyouowncocktail_g2.domain.model.Drink


sealed class DrinkAction {
    data class GotoDetail(val drinkID: Int) : DrinkAction()
    data class SetPref(val drink: Drink, val boolean: Boolean) :
        DrinkAction()
}