package co.develhope.chooseyouowncocktail_g2.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.develhope.chooseyouowncocktail_g2.DrinkAction
import co.develhope.chooseyouowncocktail_g2.databinding.DrinkCardBinding
import co.develhope.chooseyouowncocktail_g2.domain.model.Drink
import co.develhope.chooseyouowncocktail_g2.setImageByUrl
import android.content.Context
import android.util.Log
import co.develhope.chooseyouowncocktail_g2.DrinkList
import co.develhope.chooseyouowncocktail_g2.R
import kotlin.collections.ArrayList


class DrinkCardAdapter(private var drinkListForAdapter: List<Drink>,
                       private val action: (DrinkAction) -> Unit
) : RecyclerView.Adapter<DrinkCardAdapter.ViewHolder>() {
    private lateinit var binding: DrinkCardBinding
    private lateinit var context:Context


    inner class ViewHolder(val binding: DrinkCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DrinkCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context= parent.context

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int,) {

        with(receiver = holder) {
            with(drinkListForAdapter[position]) {
                binding.drinkName.text = this.name

                binding.drinkShortDescription.text = this.shortDescription
                showPreferiteRecycleView(this, holder)

                binding.drinkImage.setImageByUrl(
                    "https://www.thecocktaildb.com/images/media/drink/metwgh1606770327.jpg",
                    60,
                    60
                )

                binding.buttonGoToDetail.setOnClickListener {
                    action(DrinkAction.GotoDetail(this.id))
                }

               binding.drinkFavourite.setOnClickListener {
                   this.favourite = !this.favourite
                   showPreferiteRecycleView(this, holder)
                   action(DrinkAction.SetPref(this, this.favourite))
                   if(this.favourite)
                       preferDrink(position, 0)
                   else
                       unPreferDrink(position)

               }
            }


        }}

    private fun preferDrink(toRemove: Int, toInsert: Int) {

        val drink = (drinkListForAdapter as ArrayList<Drink>).removeAt(toRemove)
        notifyDataSetChanged()
        (drinkListForAdapter as ArrayList<Drink>).add(toInsert, drink)
        notifyDataSetChanged()

    }


    private fun unPreferDrink(toRemove: Int){
        val drink = (drinkListForAdapter as ArrayList<Drink>).removeAt(toRemove)
        var i = 0

        val drinkfalse = drinkListForAdapter.filter{!it.favourite}
        val drinktrue = drinkListForAdapter.filter{it.favourite}

        if (drinkfalse.isNotEmpty()){
            drinkfalse.forEach{ if (it.sortingPosition < drink.sortingPosition) i += 1}
            i+=drinktrue.size
        }
        else
            i = drinkListForAdapter.size
        notifyDataSetChanged()
        (drinkListForAdapter as ArrayList<Drink>).add(i, drink)
        notifyDataSetChanged()
    }

    private fun showPreferiteRecycleView(drink: Drink, holder: ViewHolder){
        if (drink.favourite){
            holder.binding.drinkFavourite.setBackgroundResource(R.drawable.ic_fav_on)

        }else{
            holder.binding.drinkFavourite.setBackgroundResource(R.drawable.ic_fav_off)
        }
    }

    override fun getItemCount(): Int {
        return drinkListForAdapter.size
    }

}


