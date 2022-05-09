package co.develhope.chooseyouowncocktail_g2

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import co.develhope.chooseyouowncocktail_g2.adapter.DrinkAction

object Action {
    fun makeActionDone(action: DrinkAction, frag: Fragment) {
        when(action){
            is DrinkAction.GotoDetail -> {
                findNavController(frag).navigate(R.id.detailDrinkFragment, bundleOf().apply {
                    putString("name", action.beer.name)
                    putString("desc", action.beer.description)
                    putInt("preview", action.beer.img)
                    putString("cl",action.beer.cl.toString() + " cl")
                    putBoolean("favorite", action.beer.favourite)
                    putString("currentPage", "Home")
                })
            }
           // is DrinkAction.SetPref -> TODO()
        }
    }
}