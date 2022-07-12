package co.develhope.chooseyouowncocktail_g2.ui.search

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
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    private lateinit var drinkCardAdapter: DrinkCardAdapter

    private val viewModel: SearchViewModel by inject()

    private var isLoading = false

    private lateinit var drinksList: List<Drink>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        drinksList = viewModel.drinkList.getFavorite().ifEmpty { viewModel.drinkList.getList() }


        if (viewModel.drinkList.getList().isEmpty()) {
            retrieveFromDB()
        }

        apiRetrieveObserver()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drinkCardAdapter = DrinkCardAdapter(
            drinksList
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

    private fun search(queryTyping: String?) {
        val filteredList =
            queryTyping?.let { viewModel.filterList(viewModel.drinkList.getList(), it) }
        if (filteredList != null) {
            if (filteredList.isNotEmpty()) {
                showResultUI(filteredList)
            } else {
                nothingFoundUI()
            }
        }
    }

    private fun initStateUI() {
        drinkCardAdapter.updateAdapterList(drinksList)
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
                        isLoading = true
                    }
                    is DBResult.Result -> {
                        if (isLoading) {
                            viewModel.increaseCurrentLetter()
                            drinkCardAdapter.updateAdapterList(viewModel.drinkList.getList())
                            drinkCardAdapter.notifyDataSetChanged()
                            isLoading = false
                        }
                    }
                    is DBResult.Error -> {
                        activity?.let {
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
                    }
                    is DBResult.NullResult -> {
                        viewModel.increaseCurrentLetter()
                        retrieveFromDB()
                    }
                }
            }
        }
    }

    private fun makeActionDone(action: DrinkAction) {
        when (action) {
            is DrinkAction.GotoDetail -> {
                viewModel.getByID(action.drinkID).let {
                    if (it != null) {
                        val detailDrinkFragment = DetailDrinkFragment
                        (activity as MainActivity).goToFragment(
                            detailDrinkFragment.newInstance(it) { action -> makeActionDone(action) },
                            detailDrinkFragment.fragmentTag
                        )
                    } else {
                        Toast.makeText(
                            context,
                            R.string.somethingWrong, Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            }
            is DrinkAction.SetPref -> {
                viewModel.drinkList.setFavorite(action.drink, action.boolean)
                if (action.boolean) {
                    viewModel.moveItem(
                        action.drink,
                        0
                    )
                    drinkCardAdapter.notifyItemMoved(viewModel.getFromPos(action.drink), 0)
                    if (viewModel.drinkList.getFavorite().isNotEmpty()) {
                        drinkCardAdapter.updateAdapterList(viewModel.drinkList.getFavorite())
                    }
                    drinkCardAdapter.notifyDataSetChanged()
                } else {
                    val originPos = viewModel.restoreOriginPos(action.drink)
                    drinkCardAdapter.notifyItemMoved(
                        viewModel.getFromPos(action.drink),
                        originPos
                    )
                    if (viewModel.drinkList.getFavorite().isEmpty()) {
                        drinkCardAdapter.updateAdapterList(viewModel.drinkList.getList())
                    }
                    drinkCardAdapter.notifyDataSetChanged()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}