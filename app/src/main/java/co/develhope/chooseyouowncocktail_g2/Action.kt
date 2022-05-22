package co.develhope.chooseyouowncocktail_g2

import android.widget.Toast
import co.develhope.chooseyouowncocktail_g2.model.Beer

sealed class DrinkAction {
    data class GotoDetail(val beer: Beer?) : DrinkAction()
    object SetPref : DrinkAction()
}

object Action {

    lateinit var activity: MainActivity

    fun makeActionDone(action: DrinkAction) {
        when (action) {
            is DrinkAction.GotoDetail -> {
                if (action.beer != null) {
                    activity.goToFragment(
                        DetailDrinkFragment.newInstance(action.beer)
                    )
                } else {
                    Toast.makeText(activity, "An error has occurred", Toast.LENGTH_LONG).show()
                }
            }
            DrinkAction.SetPref -> TODO()
        }
    }
}