package co.develhope.chooseyouowncocktail_g2.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.develhope.chooseyouowncocktail_g2.DrinkAction
import co.develhope.chooseyouowncocktail_g2.DrinkList
import co.develhope.chooseyouowncocktail_g2.R
import co.develhope.chooseyouowncocktail_g2.databinding.DrinkCardBinding
import co.develhope.chooseyouowncocktail_g2.databinding.LoadingRingBinding
import co.develhope.chooseyouowncocktail_g2.setImageByUrl


class DrinkCardAdapter(
    val action: (DrinkAction) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class DrinkHolder(binding: DrinkCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ProgressRingHolder(binding: LoadingRingBinding) :
        RecyclerView.ViewHolder(binding.root)

   var beerListForAdapter = DrinkList.drinkList()

    private lateinit var context: Context

    private var currentPos = 0

    private var isLoading = false

    companion object {
        private const val viewTypeDrinkCard = 0
        private const val viewTypeLoadingRing = 1
    }

    private lateinit var drinkBinding: DrinkCardBinding
    private lateinit var loadingBinding: LoadingRingBinding




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        context = parent.context

        if (viewType == viewTypeDrinkCard) {
            drinkBinding = DrinkCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return DrinkHolder(
                drinkBinding
            )
        } else {
            println("loading bar")
            loadingBinding = LoadingRingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ProgressRingHolder(
                loadingBinding
            )

        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        currentPos = holder.position

        if (getItemViewType(position)== viewTypeDrinkCard) {

            with(beerListForAdapter[position]) {
                if (this != null) {
                    drinkBinding.drinkName.text = this.name

                    drinkBinding.drinkCl.text = this.category

                    drinkBinding.drinkShortDescription.text = this.shortDescription

                    drinkBinding.drinkImage.setImageByUrl(this.img,
                        100,
                        100
                    )

                    drinkBinding.buttonGoToDetail.setOnClickListener {
                        action(DrinkAction.GotoDetail(this.id))
                    }

                    var switch = false
                    val bf = drinkBinding.drinkFavourite

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
        } else {
            print("loading ring")
            loadingBinding.loadingRing.visibility = View.VISIBLE
        }
    }

    fun getCurrentPosition(): Int {
        return currentPos
    }


    override fun getItemCount(): Int {
        return DrinkList.drinkList().size
    }

    fun setLoadingState(boolean: Boolean) {
        isLoading = boolean
    }


    override fun getItemViewType(position: Int): Int {
        return if (DrinkList.drinkList().get(position) == null) viewTypeLoadingRing
        else viewTypeDrinkCard    }

}


