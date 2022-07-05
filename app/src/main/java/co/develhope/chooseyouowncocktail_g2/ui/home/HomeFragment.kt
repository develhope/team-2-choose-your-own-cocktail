package co.develhope.chooseyouowncocktail_g2.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import co.develhope.chooseyouowncocktail_g2.DrinkAction
import co.develhope.chooseyouowncocktail_g2.DrinkList
import co.develhope.chooseyouowncocktail_g2.DrinkList.drinkList
import co.develhope.chooseyouowncocktail_g2.DrinkList.setFavorite
import co.develhope.chooseyouowncocktail_g2.MainActivity
import co.develhope.chooseyouowncocktail_g2.ViewModelFactory
import co.develhope.chooseyouowncocktail_g2.adapter.DrinkCardAdapter
import co.develhope.chooseyouowncocktail_g2.adapter.HeaderAdapter
import co.develhope.chooseyouowncocktail_g2.adapter.LoaderAdapter
import co.develhope.chooseyouowncocktail_g2.databinding.FragmentHomeBinding


import co.develhope.chooseyouowncocktail_g2.ui.DetailDrinkFragment


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var drinkCardAdapter: DrinkCardAdapter
    private lateinit var headerAdapter: HeaderAdapter
    private lateinit var loaderAdapter: LoaderAdapter
    private lateinit var concatAdapter: ConcatAdapter

    private lateinit var backPressedCallback: OnBackPressedCallback

    private val viewModel =
        ViewModelFactory().create(HomeViewModel::class.java)

    private var isLoading = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.list.observe(viewLifecycleOwner) {
            drinkCardAdapter.updateAdapterList(it)
            drinkCardAdapter.notifyItemRangeChanged(
                0,
                drinkCardAdapter.itemCount
            )
        }

        if (drinkList().isEmpty()) {
            binding.loadingRingEmpty.visibility = VISIBLE
            retrieveFromDB()
        } else {
            viewModel.list.value?.let { drinkCardAdapter.updateAdapterList(it) }
        }

        onLastItemLoadMore()

        apiRetrieveObserver()

        backPressedCallback = onBackPressScrollToTop()

    }

    private fun apiRetrieveObserver() {
        viewModel.result.observe(viewLifecycleOwner) { result ->
            when (result) {
                is DBResult.Loading -> {
                    loaderAdapter.updateLoadingRing(true)
                    isLoading = true
                }
                is DBResult.Result -> {
                    if (isLoading) {

                        DrinkList.addToDrinkList(result.db)
                        updateRecyclerView()
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.loadingRingEmpty.visibility = GONE
                        }, 1000)

                        isLoading = false
                    }
                }
                is DBResult.Error -> {
                    onLastItemLoadMore()
                }
            }
        }
    }

    private fun updateRecyclerView() {
        drinkCardAdapter.updateAdapterList(drinkList())
        //Delay per ragioni estetiche, altrimenti il loading ring non si vede
        Handler(Looper.getMainLooper()).postDelayed({
            loaderAdapter.updateLoadingRing(false)
            drinkCardAdapter.notifyDataSetChanged()
        }, 500)
    }

    private fun onLastItemLoadMore() {
        binding.drinkCardRecyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1) && drinkList().isNotEmpty()
                    ) {
                        retrieveFromDB()
                    }
                }
            })
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


    private fun onBackPressScrollToTop(): OnBackPressedCallback {
        return requireActivity().onBackPressedDispatcher.addCallback {
            if (drinkCardAdapter.getCurrentPosition() in 1..50) {
                binding.drinkCardRecyclerView.smoothScrollToPosition(0)
            } else {
                binding.drinkCardRecyclerView.scrollToPosition(0)
            }
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
                setFavorite(action.drink, action.boolean)
                if (action.boolean) {
                    viewModel.moveItem(
                        action.drink,
                        0
                    )
                    drinkCardAdapter.notifyItemMoved(action.fromPos, 0)
                } else {
                    val destPos=viewModel.restoreOriginPos(action.drink)
                    drinkCardAdapter.notifyItemMoved(action.fromPos, destPos)
                }
            }
        }
    }

    private fun initRecyclerView() {
        headerAdapter = HeaderAdapter()
        loaderAdapter = LoaderAdapter()

        drinkCardAdapter =
            DrinkCardAdapter(drinkList()) { action -> makeActionDone(action) }

        concatAdapter = ConcatAdapter(headerAdapter, drinkCardAdapter, loaderAdapter)

        binding.drinkCardRecyclerView.adapter = concatAdapter


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


