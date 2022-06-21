package co.develhope.chooseyouowncocktail_g2.adapter


import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView

import co.develhope.chooseyouowncocktail_g2.DrinkAction


import co.develhope.chooseyouowncocktail_g2.R
import co.develhope.chooseyouowncocktail_g2.databinding.DrinkCardBinding
import co.develhope.chooseyouowncocktail_g2.domain.model.Drink
import co.develhope.chooseyouowncocktail_g2.setImageByUrl

import android.content.Context
import android.content.res.Resources
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat


class DrinkCardAdapter(

    private val resources: Resources,
    private val beerListForAdapter: List<Drink>,
    val action: (DrinkAction) -> Unit
) : RecyclerView.Adapter<DrinkCardAdapter.ViewHolder>() {
    private lateinit var binding: DrinkCardBinding
    inner class ViewHolder(val binding: DrinkCardBinding) : RecyclerView.ViewHolder(binding.root)
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DrinkCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(receiver = holder) {
            with(beerListForAdapter[position]) {
                binding.drinkName.text = this.name

                binding.drinkCl.text = this.category

                binding.drinkShortDescription.text = this.shortDescription

                binding.drinkImage.setImageByUrl(
                    this.img,
                    resources.getDimension(R.dimen.drink_image_dim).toInt(),
                    resources.getDimension(R.dimen.drink_image_dim).toInt(),
                )

                binding.buttonGoToDetail.setOnClickListener {
                    action(DrinkAction.GotoDetail(this.id))
                }

                var switch = false
                val bf = binding.drinkFavourite

                bf.setOnClickListener {
                    switch = if (!switch) {
                        bf.setBackgroundResource(R.drawable.ic_fav_on)
                        true
                    } else {
                        bf.setBackgroundResource(R.drawable.ic_fav_off)
                        false
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return beerListForAdapter.size
    }

}


