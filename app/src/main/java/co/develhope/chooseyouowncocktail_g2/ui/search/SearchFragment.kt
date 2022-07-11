package co.develhope.chooseyouowncocktail_g2.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

import co.develhope.chooseyouowncocktail_g2.*
import co.develhope.chooseyouowncocktail_g2.adapter.DrinkCardAdapter
import co.develhope.chooseyouowncocktail_g2.databinding.FragmentSearchBinding
import co.develhope.chooseyouowncocktail_g2.ui.home.DBEvent
import co.develhope.chooseyouowncocktail_g2.ui.home.DBResult
import co.develhope.chooseyouowncocktail_g2.DrinkList.drinkList
import co.develhope.chooseyouowncocktail_g2.domain.model.Drink
import co.develhope.chooseyouowncocktail_g2.ui.DetailDrinkFragment

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    private val drinkList = drinkList()

    private val binding get() = _binding!!

    private lateinit var startList: List<Drink>

    private lateinit var drinkCardAdapter: DrinkCardAdapter

    private val viewModel =
        ViewModelFactory().create(SearchViewModel::class.java)

    private var isLoading = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        if (drinkList().isEmpty()) {
            retrieveFromDB()
        }

        apiRetrieveObserver()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        startList = DrinkList.getFavorite().ifEmpty { drinkList }

        drinkCardAdapter = DrinkCardAdapter(
            startList
        ) { action -> makeActionDone(action) }

        initStateUI()

        binding.searchResultRC.adapter = drinkCardAdapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(queryTyping: String?): Boolean {
                if (queryTyping != null) {
                    if (queryTyping.isNotEmpty()) {
                        search(queryTyping)
                    } else {
                        initStateUI()
                    }
                }
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                search(query)
                return true
            }
        })
    }

    private fun apiRetrieveObserver() {
        viewModel.result.observe(viewLifecycleOwner) { result ->
            when (result) {
                is DBResult.Loading -> {
                    isLoading = true
                }
                is DBResult.Result -> {
                    if (isLoading) {

                        DrinkList.addToDrinkList(result.db)

                        drinkCardAdapter.updateAdapterList(drinkList())

                        isLoading = false
                    }
                }
                is DBResult.Error -> {
                    retrieveFromDB()
                }
            }
        }
    }

    private fun search(queryTyping: String?) {
        val filteredList = queryTyping?.let { viewModel.filterList(drinkList, it) }
        if (filteredList != null) {
            if (filteredList.isNotEmpty()) {
                showResultUI(filteredList)
            } else {
                nothingFoundUI()
            }
        }
    }

    private fun initStateUI() {
        drinkCardAdapter.updateAdapterList(drinkList())
        binding.resultCount.visibility = View.GONE
        binding.empty.visibility = View.GONE
        binding.searchResultRC.visibility = View.VISIBLE
    }

    private fun showResultUI(filteredList: List<Drink>) {
        binding.resultCount.text =
            filteredList.size.toString() + " " +
                    resources.getString(R.string.results)
        drinkCardAdapter.updateAdapterList(filteredList)
        drinkCardAdapter.notifyDataSetChanged()
        binding.searchResultRC.visibility = View.VISIBLE
        binding.resultCount.visibility = View.VISIBLE
        binding.empty.visibility = View.GONE
    }

    private fun nothingFoundUI() {
        binding.searchResultRC.visibility = View.GONE
        binding.resultCount.visibility = View.GONE
        binding.empty.visibility = View.VISIBLE
    }


    private fun retrieveFromDB() {
        if (viewModel.result.value != DBResult.Loading) {
            viewModel.send(
                DBEvent.RetrieveDrinksByFirstLetter(
                    DrinkList.currentLetter[DrinkList.letterIndex]
                )
            )
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
                DrinkList.setFavorite(action.drink, action.boolean)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}