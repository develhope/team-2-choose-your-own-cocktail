package co.develhope.chooseyouowncocktail_g2.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import co.develhope.chooseyouowncocktail_g2.*
import co.develhope.chooseyouowncocktail_g2.adapter.DrinkCardAdapter
import co.develhope.chooseyouowncocktail_g2.databinding.FragmentSearchBinding
import co.develhope.chooseyouowncocktail_g2.domain.model.Drink
import co.develhope.chooseyouowncocktail_g2.ui.DetailDrinkFragment


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null


    private val drinkList = DrinkList.drinkList()


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //private lateinit var viewModel: ProfileViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.empty.visibility=View.GONE
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(queryTyping: String?): Boolean {


                return true
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.searchView.clearFocus()
                val filteredList = drinkList.filterList(binding.searchView.query.toString())
                //Sostituire la stringa in hardcode


                if (filteredList.isNotEmpty()) {
                    binding.resultCount.text =
                        filteredList.size.toString() + " " +
                                resources.getString(R.string.results)


                    binding.searchResultRC.visibility=View.VISIBLE
                    binding.empty.visibility=View.GONE
                    binding.searchResultRC.adapter = DrinkCardAdapter(
                        //requireActivity(),
                        filteredList
                    ) { action -> makeActionDone(action) }

                } else {
                    binding.searchResultRC.visibility=View.GONE
                    binding.empty.visibility=View.VISIBLE
                    binding.resultCount.visibility=View.GONE
                }

                return true
            }
        })
    }


    private fun List<Drink>.filterList(query: String): List<Drink> {
        return this.filter {
            it.name!!.contains(query, true) ||
                    it.description!!.contains(query, true) ||
                    it.shortDescription!!.contains(query, true)

        }
    }

    private fun makeActionDone(action: DrinkAction) {
        when (action) {
            is DrinkAction.GotoDetail -> {
                action.drinkID.let {
                    val detailDrinkFragment = DetailDrinkFragment
                    (activity as MainActivity).goToFragment(
                        detailDrinkFragment.newInstance(it),
                        detailDrinkFragment.fragmentTag
                    )
                }
            }
            is DrinkAction.SetPref -> {
                DrinkList.setFavorite(action.drink, action.drinkPref)
                DrinkList.booleanSortDrinkList()

                DrinkList.drinkList().forEach{Log.d("debug", "${it.name} and ${it.favourite}") }

            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
