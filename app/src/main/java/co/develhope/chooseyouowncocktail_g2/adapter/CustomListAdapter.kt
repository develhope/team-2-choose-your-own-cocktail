package co.develhope.chooseyouowncocktail_g2.adapter

import co.develhope.chooseyouowncocktail_g2.R


import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import co.develhope.chooseyouowncocktail_g2.DrinkAction
import co.develhope.chooseyouowncocktail_g2.model.Beer
import com.squareup.picasso.Picasso

class CustomListAdapter(
    private val context: Activity,
    private val beer: List<Beer>,
    val action: (DrinkAction) -> Unit
) :
    ArrayAdapter<Beer>(context, R.layout.list_item, beer) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val view = inflater.inflate(R.layout.list_item, null, true)

        val beerName = view.findViewById(R.id.beerName) as TextView
        val beerCl = view.findViewById(R.id.beer_cl) as TextView
        val beerPreview = view.findViewById(R.id.img_preview) as ImageView

        view.setOnClickListener {
            action(DrinkAction.GotoDetail(beer[position]))
        }


        beerName.text = beer[position].name
        beerCl.text = beer[position].cl.toString() + " cl"

        Picasso.get()
            .load("https://www.thecocktaildb.com/images/media/drink/metwgh1606770327.jpg")
            .resize(60, 60)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(beerPreview)


        return view
    }
}