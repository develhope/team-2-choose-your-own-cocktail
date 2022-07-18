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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import co.develhope.chooseyouowncocktail_g2.*
import co.develhope.chooseyouowncocktail_g2.adapter.DrinkAction
import co.develhope.chooseyouowncocktail_g2.adapter.DrinkCardAdapter
import co.develhope.chooseyouowncocktail_g2.adapter.HeaderAdapter
import co.develhope.chooseyouowncocktail_g2.adapter.LoaderAdapter
import co.develhope.chooseyouowncocktail_g2.databinding.FragmentHomeBinding
import co.develhope.chooseyouowncocktail_g2.ui.detail.DetailDrinkFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var drinkCardAdapter: DrinkCardAdapter
    private lateinit var headerAdapter: HeaderAdapter
    private lateinit var loaderAdapter: LoaderAdapter
    private lateinit var concatAdapter: ConcatAdapter

    private lateinit var backPressedCallback: OnBackPressedCallback

    private val viewModel: HomeViewModel by inject()


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

        lifecycleScope.launch {
            viewModel.list.collect {
                drinkCardAdapter.updateAdapterList(it)
                drinkCardAdapter.notifyItemRangeChanged(
                    0,
                    drinkCardAdapter.itemCount
                )
            }
        }

        if (viewModel.drinkList.getList().isEmpty()) {
            binding.loadingRingEmpty.visibility = VISIBLE
            retrieveFromDB()
        } else {
            drinkCardAdapter.updateAdapterList(viewModel.drinkList.getList())
        }

        onLastItemLoadMore()

        apiRetrieveObserver()

        backPressedCallback = onBackPressScrollToTop()

    }

    private fun apiRetrieveObserver() {
        lifecycleScope.launch {
            viewModel.result.collect { result ->
                when (result) {
                    is DBResult.Loading -> {
                        loaderAdapter.updateLoadingRing(true)
                        viewModel.isLoading = true
                    }
                    is DBResult.Result -> {
                        if (viewModel.isLoading) {
                            viewModel.increaseCurrentLetter()
                            updateRecyclerView()
                            Handler(Looper.getMainLooper()).postDelayed({
                                binding.loadingRingEmpty.visibility = GONE
                            }, 1000)

                            viewModel.isLoading = false
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

    private fun updateRecyclerView() {
        drinkCardAdapter.updateAdapterList(viewModel.drinkList.getList())
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
                    if (!recyclerView.canScrollVertically(1) && viewModel.drinkList.getList()
                            .isNotEmpty()
                    ) {
                        if (viewModel.checkCurrentLetter()) retrieveFromDB()
                    }
                }
            })
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


    private fun onBackPressScrollToTop(): OnBackPressedCallback {
        return requireActivity().onBackPressedDispatcher.addCallback {
            if (_binding != null) {
                if (drinkCardAdapter.getCurrentPosition() in 1..50) {
                    binding.drinkCardRecyclerView.smoothScrollToPosition(0)
                } else {
                    binding.drinkCardRecyclerView.scrollToPosition(0)
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
                if (action.boolean) {
                    viewModel.moveItem(
                        action.drink,
                        0
                    )
                    drinkCardAdapter.notifyItemMoved(viewModel.getFromPos(action.drink), 0)
                } else {
                    if (viewModel.drinkList.originDrinkList()
                            .find { it.id == action.drink.id } != null
                    ) {
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
            }
        }
    }

    private fun initRecyclerView() {
        headerAdapter = HeaderAdapter()
        loaderAdapter = LoaderAdapter()
        drinkCardAdapter =
            DrinkCardAdapter(viewModel.drinkList.getList()) { action -> makeActionDone(action) }
        concatAdapter = ConcatAdapter(headerAdapter, drinkCardAdapter, loaderAdapter)
        binding.drinkCardRecyclerView.adapter = concatAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}