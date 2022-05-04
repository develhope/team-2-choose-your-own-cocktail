package co.develhope.chooseyouowncocktail_g2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.develhope.chooseyouowncocktail_g2.model.Beer
import co.develhope.chooseyouowncocktail_g2.DrinkList
import co.develhope.chooseyouowncocktail_g2.databinding.DrinkCardBinding

class DrinkCardAdapter(
    val beerListForAdapter: List<Beer>,
) : RecyclerView.Adapter<DrinkCardAdapter.ViewHolder>() {
    private lateinit var binding: DrinkCardBinding

    inner class ViewHolder(val binding: DrinkCardBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DrinkCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(receiver = holder) {

            with(DrinkList.beerList()[position]) {
                binding.drinkName.text = this.name
                binding.drinkCl.text = this.cl.toString()
                binding.drinkShortDescription.text = this.shortDescription
                binding.drinkImage.setImageResource(this.img)
            }
        }
    }

    override fun getItemCount(): Int {
        return DrinkList.beerList().size
    }
}
