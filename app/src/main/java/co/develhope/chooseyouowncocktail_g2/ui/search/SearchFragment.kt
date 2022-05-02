package co.develhope.chooseyouowncocktail_g2.ui.search

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import co.develhope.chooseyouowncocktail_g2.DrinkList
import co.develhope.chooseyouowncocktail_g2.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    private val drinkList = DrinkList.beerList()
    private val beerNameList = extractBeerName()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    companion object {
        fun newInstance() = SearchFragment()
    }

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


        val adapter = activity?.let {
            ArrayAdapter(
                it,
                R.layout.simple_list_item_1,
                beerNameList
            )
        }

        binding.listView.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(queryTyping: String?): Boolean {
                adapter?.filter?.filter(queryTyping)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                if (beerNameList.contains(query)) {
                    adapter?.filter?.filter(query)
                } else {
                    Toast.makeText(context, "nothing found", Toast.LENGTH_LONG).show()
                }
                return false
            }
        })
    }

    private fun extractBeerName(): List<String> {
        var listBeerName = arrayListOf<String>()
        for (item in drinkList)
            listBeerName.add(item.name)
        return listBeerName
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}