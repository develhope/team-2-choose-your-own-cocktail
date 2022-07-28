package co.develhope.chooseyouowncocktail_g2.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.develhope.chooseyouowncocktail_g2.ListToShow
import co.develhope.chooseyouowncocktail_g2.MainActivity
import co.develhope.chooseyouowncocktail_g2.databinding.AdapterHeaderBinding
import co.develhope.chooseyouowncocktail_g2.ui.add.AddFragment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

const val ALLDRINKS = "All Drinks"
const val FAVORITEDRINKS = "Favorite Drinks"
const val MYDRINKS = "My Drinks"


class HeaderAdapter :
    RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>() {

    lateinit var mainActivity: MainActivity

    private lateinit var holder: HeaderViewHolder

    private var _listToShow = MutableStateFlow<ListToShow>(ListToShow.AllDrinks)
    val listToShow: StateFlow<ListToShow>
        get() = _listToShow

    inner class HeaderViewHolder(val binding: AdapterHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HeaderAdapter.HeaderViewHolder {
        return HeaderViewHolder(
            AdapterHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(loaderholder: HeaderViewHolder, position: Int) {

        holder = loaderholder

        updateListTitle()

        loaderholder.binding.headerText.setOnClickListener {

            val builder = AlertDialog.Builder(mainActivity)
            builder.setTitle("Choose Section")

            builder.setItems(
                arrayOf(
                    ALLDRINKS,
                    FAVORITEDRINKS,
                    MYDRINKS
                )
            ) { dialog, which ->
                when (which) {
                    0 -> {
                        _listToShow.value = ListToShow.AllDrinks
                    }
                    1 -> {
                        _listToShow.value = ListToShow.FavoriteDrinks
                    }
                    2 -> {
                        _listToShow.value = ListToShow.MyDrinks
                    }
                }
                updateListTitle()
            }
            builder.create().show()
        }


        loaderholder.binding.addButton.setOnClickListener {
            mainActivity.supportActionBar?.title = "Add a drink"
            mainActivity.goToFragment(
                AddFragment.newInstance(),
                "AddFragment"
            )
        }
    }


    private fun updateListTitle() {
        when (listToShow.value) {
            ListToShow.AllDrinks -> {
                holder.binding.headerText.text = ALLDRINKS + " ↓"
            }
            ListToShow.FavoriteDrinks -> {
                holder.binding.headerText.text = FAVORITEDRINKS + " ↓"
            }
            ListToShow.MyDrinks -> {
                holder.binding.headerText.text = MYDRINKS + " ↓"
            }
        }
    }

    override fun getItemCount(): Int {
        return 1
    }

}
