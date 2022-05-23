package co.develhope.chooseyouowncocktail_g2

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import co.develhope.chooseyouowncocktail_g2.adapter.DrinkAction

object Action {
    fun makeActionDone(action: DrinkAction, frag: Fragment) {
        when (action) {
            is DrinkAction.GotoDetail -> {
                findNavController(frag).navigate(R.id.detailDrinkFragment, bundleOf().apply {
                    putString("name", action.drink.name)
                    putString("desc", action.drink.description)
                    putString("preview", action.drink.img)
                    //putString("cl",action.drink.cl.toString() + " cl")

                    putString("currentPage", "Home")
                })
            }
            DrinkAction.SetPref -> TODO()
        }
    }
}