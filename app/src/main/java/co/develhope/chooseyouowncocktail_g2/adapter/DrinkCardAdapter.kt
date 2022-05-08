package co.develhope.chooseyouowncocktail_g2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import co.develhope.chooseyouowncocktail_g2.R
import co.develhope.chooseyouowncocktail_g2.databinding.DrinkCardBinding
import co.develhope.chooseyouowncocktail_g2.model.Beer
import co.develhope.chooseyouowncocktail_g2.BuildConfig
import co.develhope.chooseyouowncocktail_g2.DetailDrinkFragment
import co.develhope.chooseyouowncocktail_g2.ui.home.HomeFragment


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
            with(beerListForAdapter[position]) {
                binding.drinkName.text = this.name
                binding.drinkCl.text = this.cl.toString() + " cl"
                binding.drinkShortDescription.text = this.shortDescription
                binding.drinkImage.setImageResource(this.img)
                binding.buttonGoToDetail.setOnClickListener {
                    val bundle = bundleOf("drink name" to this.name,
                                            "drinkCl" to this.cl.toString(),
                                            "short description" to this.shortDescription,
                                            "set ImgRes" to this.img)
                    it.findNavController().navigate(R.id.detailDrinkFragment, bundle)

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return beerListForAdapter.size
    }

}
