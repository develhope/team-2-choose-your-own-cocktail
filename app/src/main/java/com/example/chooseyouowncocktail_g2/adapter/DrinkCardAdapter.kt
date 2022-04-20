package com.example.chooseyouowncocktail_g2.adapter

import android.graphics.Typeface
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.util.DisplayMetrics
import androidx.recyclerview.widget.RecyclerView
import com.example.chooseyouowncocktail_g2.model.Beer
import com.example.chooseyouowncocktail_g2.DrinkList
import com.example.chooseyouowncocktail_g2.DrinkList.beers
import com.bumptech.glide.Glide
import android.content.res.AssetManager

import com.example.chooseyouowncocktail_g2.databinding.DrinkCardBinding

class DrinkCardAdapter(
    var languageList: List<Beer>,
) : RecyclerView.Adapter<DrinkCardAdapter.ViewHolder>() {

    // create an inner class with name ViewHolder
    // It takes a view argument, in which pass the generated class of single_item.xml
    // ie SingleItemBinding and in the RecyclerView.ViewHolder(binding.root) pass it like this
    inner class ViewHolder(val binding: DrinkCardBinding) : RecyclerView.ViewHolder(binding.root)

    // inside the onCreateViewHolder inflate the view of SingleItemBinding
    // and return new ViewHolder object containing this layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DrinkCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    // bind the items with each item
    // of the list languageList
    // which than will be
    // shown in recycler view
    // to keep it simple we are
    // not setting any image data to view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(receiver = holder){
            with(beers[position]){
                binding.drinkName.text = this.name
                binding.drinkCl.text=this.cl.toString()
                binding.drinkShortDescription.text = this.shortDescription



                val setTextNameSize=binding.drinkName
                setTextNameSize.setTextSize(18f)
                val setDrinkImage=binding.drinkImage
                setDrinkImage.setImageResource(this.img)
                val setTextClSize=binding.drinkCl
                setTextClSize.setTextSize(16f)
                val setShortDescriptionSize=binding.drinkShortDescription
                setShortDescriptionSize.setTextSize(12f)
                setTextNameSize.gravity=0
                setTextClSize.gravity=0
                setShortDescriptionSize.gravity= 0
                // val textNameFont Typeface.createFromAsset(this.getAssets(),"@fonts/poppins_semibold.ttf")
                // setTextNameSize.setTypeface(textNameFont )








            }
        }
    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return beers.size
    }
}
