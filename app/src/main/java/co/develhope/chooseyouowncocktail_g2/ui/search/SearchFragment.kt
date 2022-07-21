package co.develhope.chooseyouowncocktail_g2.ui.search

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import co.develhope.chooseyouowncocktail_g2.*
import co.develhope.chooseyouowncocktail_g2.adapter.DrinkCardAdapter
import co.develhope.chooseyouowncocktail_g2.databinding.FragmentSearchBinding
import co.develhope.chooseyouowncocktail_g2.usecase.model.Drink
import co.develhope.chooseyouowncocktail_g2.ui.detail.DetailDrinkFragment
import co.develhope.chooseyouowncocktail_g2.ui.home.DBEvent
import co.develhope.chooseyouowncocktail_g2.ui.home.DBResult
import co.develhope.chooseyouowncocktail_g2.adapter.DrinkAction
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    private lateinit var drinkCardAdapter: DrinkCardAdapter

    private val viewModel: SearchViewModel by inject()
    lateinit var preferences: SharedPreferences

    private var gson = Gson()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        if (viewModel.drinkList.getList().isEmpty()) {
            retrieveFromDB()
        }

        apiRetrieveObserver()

        searchObserver()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drinkCardAdapter = DrinkCardAdapter(
            emptyList()
        ) { action -> makeActionDone(action) }

        initStateUI()

        binding.searchResultRC.adapter = drinkCardAdapter

        binding.searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(queryTyping: String?): Boolean {

                    if (queryTyping != null) {
                        if (queryTyping.isNotEmpty()) {
                            searchOnDB(queryTyping)
                        } else {
                            initStateUI()
                        }
                    }
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    searchOnDB(query)
                    return true
                }

            })
    }

    private fun initStateUI() {
        viewModel.resultList = if (binding.searchView.query.isEmpty()) {
            viewModel.drinkList.getFavorite().ifEmpty {
                viewModel.drinkList.getList()
            }
        } else {
            viewModel.search.value.let { viewModel.resultList }
        }
        drinkCardAdapter.updateAdapterList(
            viewModel.resultList
        )
        drinkCardAdapter.notifyDataSetChanged()

        binding.resultCount.visibility = View.GONE
        binding.empty.visibility = View.GONE
    }

    private fun showResultUI(resultList: List<Drink>) {
        binding.resultCount.text =
            viewModel.resultList.size.toString() + " " +
                    resources.getString(R.string.results)
        drinkCardAdapter.updateAdapterList(resultList)
        drinkCardAdapter.notifyDataSetChanged()
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
                    viewModel.drinkList.indexLetter[viewModel.drinkList.letterIndex]
                )
            )
        }
    }

    private fun apiRetrieveObserver() {
        lifecycleScope.launch {
            viewModel.result.collect { result ->
                when (result) {
                    is DBResult.Loading -> {
                        viewModel.isLoading = true
                    }
                    is DBResult.Result -> {
                        if (viewModel.isLoading) {
                            viewModel.increaseCurrentLetter()

                            drinkCardAdapter.updateAdapterList(viewModel.drinkList.getList())
                            drinkCardAdapter.notifyDataSetChanged()
                            viewModel.isLoading = false
                        }
                    }
                    is DBResult.Error -> {
                        activity?.let { activity ->
                            Snackbar.make(
                                activity.findViewById(android.R.id.content),
                                R.string.retrieveError,
                                Snackbar.LENGTH_INDEFINITE
                            )
                                .setAction(R.string.retry) {
                                    retrieveFromDB()
                                }
                                .show()
                        }

                    }
                    is DBResult.NullResult -> {
                        viewModel.increaseCurrentLetter()
                        retrieveFromDB()
                    }
                }
            }
        }
    }

    private fun searchOnDB(query: String) {
        if (viewModel.search.value != SearchResult.Loading) {
            viewModel.send(
                DBEvent.RetrieveDrinksByName(query)
            )
        }
    }

    private fun searchObserver() {
        lifecycleScope.launch {
            viewModel.search.collect { result ->
                when (result) {
                    is SearchResult.Loading -> {
                        viewModel.isLoading = true
                    }
                    is SearchResult.Result -> {
                        if (viewModel.isLoading) {

                            viewModel.resultList = viewModel.checkExistingFavorite(result.result)
                            showResultUI(viewModel.resultList)
                            drinkCardAdapter.notifyDataSetChanged()
                            viewModel.isLoading = false
                            binding.searchResultRC.visibility = View.VISIBLE
                        }
                    }
                    is SearchResult.Error -> {
                        activity?.let { activity ->
                            Snackbar.make(
                                activity.findViewById(android.R.id.content),
                                R.string.retrieveError,
                                Snackbar.LENGTH_INDEFINITE
                            )
                                .show()
                        }
                    }
                    is SearchResult.NullResult -> {
                        nothingFoundUI()
                    }
                }
            }
        }
    }

    private fun makeActionDone(action: DrinkAction) {
        when (action) {
            is DrinkAction.GotoDetail -> {
                action.drink.let {
                    val detailDrinkFragment = DetailDrinkFragment
                    (activity as MainActivity).goToFragment(
                        detailDrinkFragment.newInstance(it) { action -> makeActionDone(action) },
                        detailDrinkFragment.fragmentTag
                    )

                }
            }
            is DrinkAction.SetPref -> {
                viewModel.drinkList.setFavorite(action.drink, action.boolean)
                if (!viewModel.drinkList.getList().contains(action.drink)) {
                    viewModel.setFavoriteOnSearchResult(
                        viewModel.resultList,

                        action.drink,
                        action.boolean
                    )

                }
                if (action.boolean) {
                    viewModel.getByID(action.drink.id).let {
                        if (it != null) {
                            viewModel.moveItem(
                                action.drink,
                                0

                            )
                            drinkCardAdapter.notifyItemMoved(viewModel.getFromPos(action.drink), 0)

                        } else {
                            viewModel.addItem(action.drink)
                            drinkCardAdapter.notifyItemInserted(0)
                        }
                    }
                    if (binding.searchView.query.isEmpty() &&
                        viewModel.drinkList.getFavorite().isNotEmpty()
                    ) {
                        drinkCardAdapter.updateAdapterList(viewModel.drinkList.getFavorite())
                    }

                    drinkCardAdapter.notifyDataSetChanged()
                } else {
                    viewModel.drinkList.originDrinkList().find { it.id == action.drink.id }.let {
                        if (it != null) {
                            val originPos = viewModel.restoreOriginPos(action.drink)
                            drinkCardAdapter.notifyItemMoved(
                                viewModel.getFromPos(action.drink),
                                originPos
                            )
                        } else {
                            viewModel.removeItem(action.drink)
                            drinkCardAdapter.notifyDataSetChanged()
                        }
                    }
                    if (binding.searchView.query.isNotEmpty()) {
                        searchOnDB(binding.searchView.query.toString())
                    } else {
                        if (viewModel.drinkList.getFavorite().isEmpty()) {
                            drinkCardAdapter.updateAdapterList(viewModel.drinkList.getList())
                        } else {
                            drinkCardAdapter.updateAdapterList(viewModel.drinkList.getFavorite())
                        }

                    }
                    drinkCardAdapter.notifyDataSetChanged()

                }
                val drinkListjson = gson.toJson(viewModel.drinkList)
                val editor: SharedPreferences.Editor = preferences.edit()
                editor.putString("pref", drinkListjson)
                editor.commit()
            }
        }
    }

    override fun onStart() {
        if (binding.searchView.query.isEmpty()) {
            binding.searchResultRC.visibility = View.VISIBLE
        } else {
            binding.searchResultRC.visibility = View.GONE
        }
        super.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}