package co.develhope.chooseyouowncocktail_g2.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.develhope.chooseyouowncocktail_g2.DrinkAction
import co.develhope.chooseyouowncocktail_g2.databinding.DrinkCardBinding
import co.develhope.chooseyouowncocktail_g2.domain.model.Drink
import co.develhope.chooseyouowncocktail_g2.setImageByUrl
import android.content.Context



class DrinkCardAdapter( private val beerListForAdapter: List<Drink>,
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
            with(beerListForAdapter[position]) {
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
                   action(DrinkAction.SetPref(this, this.favourite))
                   showPreferiteRecycleView(this, holder)
                   beerListForAdapter.sortedBy { it.favourite }


               }
            }


        }}


    private fun showPreferiteRecycleView(drinkCard: Drink, holder: ViewHolder){
        if (drinkCard.favourite){
            holder.binding.drinkFavourite.setBackgroundResource(holder.imagePreferiteBackgroundOn)

        }else{
            holder.binding.drinkFavourite.setBackgroundResource(holder.imagePreferiteBackgroundOff)
        }
    }

    override fun getItemCount(): Int {
        return beerListForAdapter.size
    }
}


