package co.develhope.chooseyouowncocktail_g2


sealed class DrinkAction {
    data class GotoDetail(val drinkID: Int) : DrinkAction()
    object SetPref : DrinkAction()
}