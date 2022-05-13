package co.develhope.chooseyouowncocktail_g2.adapter

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import co.develhope.chooseyouowncocktail_g2.DetailDrinkFragment
import co.develhope.chooseyouowncocktail_g2.R
import co.develhope.chooseyouowncocktail_g2.databinding.DrinkCardBinding
import co.develhope.chooseyouowncocktail_g2.model.Beer
import co.develhope.chooseyouowncocktail_g2.ui.home.HomeFragment
import java.util.Collections
import kotlinx.coroutines.delay

import android.widget.*
import kotlin.coroutines.coroutineContext
import android.content.Context
import androidx.lifecycle.lifecycleScope
import co.develhope.chooseyouowncocktail_g2.DrinkList
import kotlinx.coroutines.launch
import java.security.AccessController.getContext
import java.util.concurrent.TimeUnit

sealed class DrinkAction{
    data class GotoDetail(val beer : Beer,val currentPage: String) : DrinkAction()
    object SetPref : DrinkAction()
}

class DrinkCardAdapter(private val context: Activity,
    val beerListForAdapter: List<Beer>,
    val currentPage: String, val action: (DrinkAction) -> Unit
) : RecyclerView.Adapter<DrinkCardAdapter.ViewHolder>() {
    private lateinit var binding: DrinkCardBinding

    inner class ViewHolder(val binding: DrinkCardBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DrinkCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(receiver = holder) {
            beerListForAdapter.sortedByDescending{it.favourite}
            with(beerListForAdapter[position]) {
                binding.drinkName.text = this.name
                binding.drinkCl.text = this.cl.toString() + " cl"
                binding.drinkShortDescription.text = this.shortDescription
                binding.drinkImage.setImageResource(this.img)

                binding.buttonGoToDetail.setOnClickListener {


                    action(DrinkAction.GotoDetail(this,currentPage))


                }
                var switch:Boolean= false
                var bf=binding.drinkFavourite
                val imOn= context.getResources().getIdentifier("ic_fav_off","drawable",context.getPackageName())
                val imOff= context.getResources().getIdentifier("ic_fav_on","drawable",context.getPackageName())
                bf.setOnClickListener {
                    var numbPref:Int=0
                    var numbNotPref:Int=0

                    numbNotPref=beerListForAdapter.size-numbPref
                    val pos:Int=bindingAdapterPosition
                    DrinkList.beerList().filter{ favourite==true }.forEach{ numbPref += 1 }
                    Toast.makeText(context, "numero preferiti:${numbPref}",
                        Toast.LENGTH_LONG).show();

                    if (!switch) {
                        bf.setBackgroundResource(imOff)
                        switch = true;

                        moveOnPrefSimple(pos,numbPref)
                        DrinkList.setFavorite(this,false)


                    } else {
                        bf.setBackgroundResource(imOn)
                        switch = false
                        moveOnNotPrefSimple(pos,numbPref)
                        DrinkList.setFavorite(this,true)
                    }


                }
            }
        }}
    fun moveOnPrefDinamic(posClicked:Int){
        for(num in 0..posClicked){

            swapeItem(posClicked,num)

        }
    }
    fun moveOnNotPrefSimple(posClicked:Int,numbofpref:Int){
        swapeItem(posClicked,numbofpref-1)
                }
    fun moveOnPrefSimple(posClicked:Int,numbofpref:Int){
        swapeItem(posClicked,numbofpref)

    }
    fun moveOnNotPrefDinamic(posClicked:Int,numbofpref:Int){
        for(num in posClicked..(numbofpref+1)){

          swapeItem(num,num+1)

        }}


  


    override fun getItemCount(): Int {
        return beerListForAdapter.size
    }
    fun swapeItem(fromPosition:Int, toPosition: Int){
        //Collections.swap(beerListForAdapter,fromPosition,toPosition)
        notifyItemMoved(fromPosition,toPosition)
    }}





