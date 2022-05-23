package co.develhope.chooseyouowncocktail_g2.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.develhope.chooseyouowncocktail_g2.R
import co.develhope.chooseyouowncocktail_g2.databinding.DrinkCardBinding
import co.develhope.chooseyouowncocktail_g2.domain.model.Drink
import com.squareup.picasso.Picasso

sealed class DrinkAction {
    data class GotoDetail(val drink: Drink, val currentPage: String) : DrinkAction()
    object SetPref : DrinkAction()
}

class DrinkCardAdapter(
    private val context: Activity,

    private val beerListForAdapter: List<Drink>,
    private val currentPage: String, val action: (DrinkAction) -> Unit
) : RecyclerView.Adapter<DrinkCardAdapter.ViewHolder>() {
    private lateinit var binding: DrinkCardBinding

    inner class ViewHolder(val binding: DrinkCardBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DrinkCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(receiver = holder) {
            with(beerListForAdapter[position]) {
                binding.drinkName.text = this.name

               // binding.drinkCl.text = this.cl.toString() + " cl"

                binding.drinkShortDescription.text = this.shortDescription

                Picasso.get()
                    .load("https://www.thecocktaildb.com/images/media/drink/metwgh1606770327.jpg")
                    .resize(60, 60)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(binding.drinkImage)


                binding.buttonGoToDetail.setOnClickListener {
                    action(DrinkAction.GotoDetail(this, currentPage))
                }


                var switch = false
                val bf = binding.drinkFavourite
                val imOn =
                    context.resources.getIdentifier("ic_fav_off", "drawable", context.packageName)
                val imOff =
                    context.resources.getIdentifier("ic_fav_on", "drawable", context.packageName)
                bf.setOnClickListener {
                    if (!switch) {
                        bf.setBackgroundResource(imOn)
                        switch = true
                    } else {
                        bf.setBackgroundResource(imOff)
                        switch = false
                    }
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return beerListForAdapter.size
    }


}
