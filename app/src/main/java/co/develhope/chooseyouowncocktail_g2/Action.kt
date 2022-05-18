package co.develhope.chooseyouowncocktail_g2
import co.develhope.chooseyouowncocktail_g2.model.Beer


sealed class DrinkAction {
    data class GotoDetail(val beer: Beer, val id : Int) : DrinkAction()
    object SetPref : DrinkAction()
}

object Action {
    fun makeActionDone(action: DrinkAction, previousFrag: String, activity: MainActivity) {
        when(action){
            is DrinkAction.GotoDetail -> {
               activity.goTo( DetailDrinkFragment.newInstance(action.id),
                   "DetailDrinkFragment", previousFrag)
            }
            DrinkAction.SetPref -> TODO()
        }
    }









}