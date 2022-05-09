package co.develhope.chooseyouowncocktail_g2.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import co.develhope.chooseyouowncocktail_g2.DrinkList
import co.develhope.chooseyouowncocktail_g2.databinding.DrinkCardBinding
import co.develhope.chooseyouowncocktail_g2.model.Beer

sealed class DrinkAction{
    data class GotoDetail(val beer : Beer,val currentPage: String) : DrinkAction()
    data class SetPref(val beer : Beer, val prefButton: Button) : DrinkAction()
}

class DrinkCardAdapter(private val context: Activity,
                       var beerListForAdapter: List<Beer>,
                       val currentPage: String, val action: (DrinkAction) -> Unit
) : RecyclerView.Adapter<DrinkCardAdapter.ViewHolder>() {
    private lateinit var binding: DrinkCardBinding

    inner class ViewHolder(val binding: DrinkCardBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DrinkCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val imOn= context.getResources().getIdentifier("ic_fav_on","drawable",context.getPackageName())
        val imOff= context.getResources().getIdentifier("ic_fav_off","drawable",context.getPackageName())

        with(receiver = holder) {
            with(beerListForAdapter[position]) {
                binding.drinkName.text = this.name
                binding.drinkCl.text = this.cl.toString() + " cl"
                binding.drinkShortDescription.text = this.shortDescription
                binding.drinkImage.setImageResource(this.img)
                if (this.favourite)
                    binding.drinkFavourite.setBackgroundResource(imOn)
                else
                    binding.drinkFavourite.setBackgroundResource(imOff)


                binding.buttonGoToDetail.setOnClickListener {
                    action(DrinkAction.GotoDetail(this,currentPage))
                }

                binding.drinkFavourite.setOnClickListener {
                    this.favourite = !this.favourite
                    if (this.favourite) {
                        it.setBackgroundResource(imOn)
                    } else {
                        it.setBackgroundResource(imOff)
                    }
                }
            }
        }
    }
  


    override fun getItemCount(): Int {
        return beerListForAdapter.size
    }

    private fun InvertPrefButtonOnclick(){

    }



}
