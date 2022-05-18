package co.develhope.chooseyouowncocktail_g2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ConcatAdapter
import co.develhope.chooseyouowncocktail_g2.Action.makeActionDone
import co.develhope.chooseyouowncocktail_g2.DrinkList
import co.develhope.chooseyouowncocktail_g2.MainActivity
import co.develhope.chooseyouowncocktail_g2.adapter.DrinkCardAdapter
import co.develhope.chooseyouowncocktail_g2.adapter.HeaderAdapter
import co.develhope.chooseyouowncocktail_g2.databinding.FragmentHomeBinding

const val homeFragment = "HomeFragment"

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val headerAdapter = HeaderAdapter()

        val drinkCardAdapter = DrinkCardAdapter(
            requireActivity(),
            DrinkList.beerList(),
        ) { action -> makeActionDone(action, homeFragment, activity as MainActivity) }

        val concatAdapter = ConcatAdapter(headerAdapter, drinkCardAdapter)

        binding.drinkCardRecyclerView.adapter = concatAdapter

        val root: View = binding.root

        return root
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}