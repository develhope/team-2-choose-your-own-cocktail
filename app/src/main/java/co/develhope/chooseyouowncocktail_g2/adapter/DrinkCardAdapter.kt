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
import kotlin.collections.indexOf as indexOf


class DrinkCardAdapter(private var drinkListForAdapter: List<Drink>,
                       private val action: (DrinkAction) -> Unit
) : RecyclerView.Adapter<DrinkCardAdapter.ViewHolder>() {
    private lateinit var binding: DrinkCardBinding
    private lateinit var context:Context


    inner class ViewHolder(val binding: DrinkCardBinding) : RecyclerView.ViewHolder(binding.root){
        val imagePreferiteBackgroundOn =
            context.resources.getIdentifier("ic_fav_on", "drawable", context.packageName)
        val imagePreferiteBackgroundOff =
            context.resources.getIdentifier("ic_fav_off", "drawable", context.packageName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DrinkCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context= parent.getContext()

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int,) {

        with(receiver = holder) {
            with(drinkListForAdapter[position]) {
                binding.drinkName.text = this.name

                binding.drinkShortDescription.text = this.shortDescription
                showPreferiteRecycleView(this)

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
                   showPreferiteRecycleView(this)
                   action(DrinkAction.SetPref(this, this.favourite))
                   if(this.favourite)
                       changeUnderlingAdapterListSetSaves(position, 0)
                   else
                       changeUnderlingAdapterListUnSetSaves(position)

               }
            }


        }}

    private fun changeUnderlingAdapterListSetSaves(toReomve: Int, toInsert: Int){
        val drink =(drinkListForAdapter as ArrayList<Drink>).get(toReomve)
        (drinkListForAdapter as ArrayList<Drink>).remove(drink)
        (drinkListForAdapter as ArrayList<Drink>).add(toInsert, drink)
        this.notifyDataSetChanged()
    }

    private fun changeUnderlingAdapterListUnSetSaves(positionToRemove: Int){
        val drink = (drinkListForAdapter as ArrayList<Drink>)[positionToRemove]
        val originIndexToInsert = DrinkList.drinkList().indexOf(DrinkList.drinkList().filter { it.name == drink.name }.get(0))
        (drinkListForAdapter as MutableList<Drink>).remove(drink)
        (drinkListForAdapter as MutableList<Drink>).add(originIndexToInsert, drink)
        this.notifyDataSetChanged()

    }

    private fun showPreferiteRecycleView(drinkCard: Drink){
        if (drinkCard.favourite){
            binding.drinkFavourite.setBackgroundResource(R.drawable.ic_fav_on)

        }else{
           binding.drinkFavourite.setBackgroundResource(R.drawable.ic_fav_off)
        }
    }

    override fun getItemCount(): Int {
        return drinkListForAdapter.size
    }
}


