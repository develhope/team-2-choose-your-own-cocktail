package co.develhope.chooseyouowncocktail_g2.adapter

import co.develhope.chooseyouowncocktail_g2.usecase.model.Drink


sealed class DrinkAction {
    data class GotoDetail(val drink:Drink) : DrinkAction()
    data class SetPref(val drink: Drink, val boolean: Boolean) :
        DrinkAction()
}