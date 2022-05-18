package co.develhope.chooseyouowncocktail_g2.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import co.develhope.chooseyouowncocktail_g2.Action.makeActionDone
import co.develhope.chooseyouowncocktail_g2.DrinkList
import co.develhope.chooseyouowncocktail_g2.MainActivity
import co.develhope.chooseyouowncocktail_g2.R
import co.develhope.chooseyouowncocktail_g2.adapter.CustomListAdapter
import co.develhope.chooseyouowncocktail_g2.adapter.DrinkCardAdapter
import co.develhope.chooseyouowncocktail_g2.databinding.FragmentSearchBinding
import co.develhope.chooseyouowncocktail_g2.model.Beer

const val searchFragment = "SearchFragment"

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    private val drinkList = DrinkList.beerList()

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


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(queryTyping: String?): Boolean {
                var filteredList = emptyList<Beer>()
                if (queryTyping!!.isNotEmpty()) {
                    filteredList = drinkList.filterList(queryTyping.toString())
                    binding.resultPreview.visibility = View.VISIBLE
                } else {
                    filteredList = emptyList<Beer>()
                    binding.resultPreview.visibility = View.GONE
                }
                binding.resultPreview.adapter = CustomListAdapter(
                    requireActivity(),
                    filteredList
                ) { action -> makeActionDone(action, searchFragment, activity as MainActivity) }

                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                binding.searchView.clearFocus()
                binding.resultPreview.visibility = View.GONE

                val filteredList = drinkList.filterList(query)

                //Sostituire la stringa in hardcode
                binding.resultCount.text =
                    filteredList.size.toString() + " " +
                            resources.getString(R.string.results)

                if (filteredList.isNotEmpty()) {

                    binding.searchResultRC.adapter = DrinkCardAdapter(
                        requireActivity(),
                        filteredList
                    ) { action -> makeActionDone(action, searchFragment,activity as MainActivity) }

                } else {
                    Toast.makeText(context, "Nothing Found", Toast.LENGTH_LONG).show()
                }

                return true
            }
        })
    }

    private fun List<Beer>.filterList(query: String): List<Beer> {
        return this.filter {
            it.name.contains(query, true) ||
                    it.description.contains(query, true) ||
                    it.shortDescription.contains(query, true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}