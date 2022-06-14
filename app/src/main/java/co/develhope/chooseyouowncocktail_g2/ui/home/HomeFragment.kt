package co.develhope.chooseyouowncocktail_g2.ui.home

import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import co.develhope.chooseyouowncocktail_g2.DrinkAction
import co.develhope.chooseyouowncocktail_g2.DrinkList

import co.develhope.chooseyouowncocktail_g2.MainActivity
import co.develhope.chooseyouowncocktail_g2.adapter.DrinkCardAdapter
import co.develhope.chooseyouowncocktail_g2.adapter.HeaderAdapter
import co.develhope.chooseyouowncocktail_g2.databinding.FragmentHomeBinding

import co.develhope.chooseyouowncocktail_g2.domain.DBViewModel
import co.develhope.chooseyouowncocktail_g2.ui.DetailDrinkFragment


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var drinkCardAdapter: DrinkCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({ inflateDrinkList() },1000)

        binding.drinkCardRecyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val isLastVisible =
                        drinkCardAdapter.getCurrentPosition() == drinkCardAdapter.itemCount
                    if (dy > 0 && isLastVisible) {
                       // (activity as MainActivity).retrieveFromDB()
                       // println("retrieve")
                     //   drinkCardAdapter.beerListForAdapter=DrinkList.drinkList()
//                        drinkCardAdapter.notifyItemRangeChanged(0,drinkCardAdapter.itemCount)
                    }
                }
            })


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
            DrinkAction.SetPref -> TODO()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun inflateDrinkList() {
        val headerAdapter = HeaderAdapter()

        drinkCardAdapter = DrinkCardAdapter { action -> makeActionDone(action) }

        DrinkList.drinkList().forEach { println(it?.name) }
        println(DrinkList.drinkList().size)

        val concatAdapter = ConcatAdapter(headerAdapter, drinkCardAdapter)

        binding.drinkCardRecyclerView.adapter = concatAdapter

        binding.loadingRing.visibility=GONE

        //concatAdapter.notifyItemRangeChanged(0,DrinkList.drinkList().size)

        //binding.loadingRing.visibility=View.GONE

    }


}


