package co.develhope.chooseyouowncocktail_g2.adapter


import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView


import co.develhope.chooseyouowncocktail_g2.databinding.DrinkCardBinding
import co.develhope.chooseyouowncocktail_g2.usecase.model.Drink

import android.content.Context
import co.develhope.chooseyouowncocktail_g2.*


const val DRINKCARD_PREVIEW_SIZE = 100


class DrinkCardAdapter(
    drinkListForAdapter: List<Drink>,
    private val action: (DrinkAction) -> Unit
) : RecyclerView.Adapter<DrinkCardAdapter.ViewHolder>() {

    private lateinit var context: Context

    private var drinkList = drinkListForAdapter
    private var currentPos = 0

    inner class ViewHolder(val binding: DrinkCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DrinkCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        currentPos = holder.absoluteAdapterPosition

        with(receiver = holder) {
            with(drinkList[position]) {
                binding.drinkName.text = this.name

                binding.drinkCl.text = this.category

                binding.drinkShortDescription.text = this.shortDescription

                binding.drinkImage.setImageByUrl(
                    this.img,
                    DRINKCARD_PREVIEW_SIZE,
                    DRINKCARD_PREVIEW_SIZE
                )

                binding.buttonGoToDetail.setOnClickListener {
                    action(DrinkAction.GotoDetail(this))
                }

                binding.drinkFavourite.setBackgroundResource(
                    showFavoriteStatus(this)
                )

                binding.drinkFavourite.setOnClickListener {
                    action(DrinkAction.SetPref(this, !this.favourite))
                }

            }

        }
    }


    private fun showFavoriteStatus(drink: Drink): Int {
        val icon = if (drink.favourite) {
            R.drawable.ic_fav_on
        } else {
            R.drawable.ic_fav_off
        }
        return icon
    }

    fun getCurrentPosition(): Int {
        return currentPos
    }

    fun updateAdapterList(newList: List<Drink>) {
        drinkList = newList
    }


    override fun getItemCount(): Int {
        return drinkList.size
    }

}

