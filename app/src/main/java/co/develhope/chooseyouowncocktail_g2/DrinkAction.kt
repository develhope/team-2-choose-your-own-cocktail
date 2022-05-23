package co.develhope.chooseyouowncocktail_g2

import co.develhope.chooseyouowncocktail_g2.model.Beer

sealed class DrinkAction {
    data class GotoDetail(val beer: Beer) : DrinkAction()
    object SetPref : DrinkAction()
}