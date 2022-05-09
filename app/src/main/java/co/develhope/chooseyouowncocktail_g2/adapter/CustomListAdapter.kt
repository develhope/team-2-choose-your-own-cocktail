package co.develhope.chooseyouowncocktail_g2.adapter

import co.develhope.chooseyouowncocktail_g2.R


import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import co.develhope.chooseyouowncocktail_g2.model.Beer

class CustomListAdapter(private val context: Activity,
                        private val beer: List<Beer>,
                        val action: (DrinkAction) -> Unit) :
    ArrayAdapter<Beer>(context, R.layout.list_item, beer) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val view = inflater.inflate(R.layout.list_item, null, true)

        val beerName = view.findViewById(R.id.beerName) as TextView
        val beerCl = view.findViewById(R.id.beer_cl) as TextView
        val beerPreview = view.findViewById(R.id.img_preview) as ImageView

        view.setOnClickListener {
            action(DrinkAction.GotoDetail(beer[position],"search"))
        }


        beerName.text = beer[position].name
        beerCl.text = beer[position].cl.toString() + " cl"
        beerPreview.setImageResource(beer[position].img)


        return view
    }
}