package com.example.chooseyouowncocktail_g2.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.chooseyouowncocktail_g2.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /* val SearchViewModel =
             ViewModelProvider(this).get(SearchViewModel::class.java)*/

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val theCocktailList = arrayListOf<String>("Old Fashioned","Negroni","Daiquiri","Dry Martini","Margarita","Espresso", "MartiniWhiskey Sour"," Manhattan")
        val adapter = activity?.let {
            ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, theCocktailList)
        }
        binding.listView.adapter = adapter

        binding.searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener {
            override fun onQueryTextChange(queryTyping: String?): Boolean {
                if (adapter != null) {
                    adapter.filter.filter(queryTyping)
                }
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                if (theCocktailList.contains(query)) {
                    if (adapter != null) {
                        adapter.filter.filter(query)
                    }
                } else {
                    Toast.makeText(context, "nothing finded", Toast.LENGTH_LONG).show()
                }
                return false
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}