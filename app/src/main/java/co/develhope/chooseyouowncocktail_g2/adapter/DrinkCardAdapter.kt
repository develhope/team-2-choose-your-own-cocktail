package co.develhope.chooseyouowncocktail_g2.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.develhope.chooseyouowncocktail_g2.R
import co.develhope.chooseyouowncocktail_g2.databinding.DrinkCardBinding
import co.develhope.chooseyouowncocktail_g2.model.Beer
import com.squareup.picasso.Picasso

sealed class DrinkAction {
    data class GotoDetail(val beer: Beer) : DrinkAction()
    object SetPref : DrinkAction()
}

class DrinkCardAdapter(
    val imOn: Int,
    val imOff: Int,
    val beerListForAdapter: List<Beer>,
    val action: (DrinkAction) -> Unit
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
                binding.drinkCl.text = this.cl.toString() + " cl"
                binding.drinkShortDescription.text = this.shortDescription

                Picasso.get()
                    .load("https://www.thecocktaildb.com/images/media/drink/metwgh1606770327.jpg")
                    .resize(60, 60)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(binding.drinkImage)


                binding.buttonGoToDetail.setOnClickListener {
                    action(DrinkAction.GotoDetail(this))
                }

                var switch = false
                val bf = binding.drinkFavourite
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
