package co.develhope.chooseyouowncocktail_g2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import co.develhope.chooseyouowncocktail_g2.DrinkList
import co.develhope.chooseyouowncocktail_g2.R
import co.develhope.chooseyouowncocktail_g2.adapter.DrinkAction
import co.develhope.chooseyouowncocktail_g2.adapter.DrinkCardAdapter
import co.develhope.chooseyouowncocktail_g2.databinding.FragmentHomeBinding

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
        val context = requireActivity()
            context.resources.getIdentifier("ic_fav_on", "drawable", context.packageName)
        val drinkCardAdapter = DrinkCardAdapter(
            DrinkList.beerList(),
        ) { action -> makeAction(action) }

        binding.drinkCardRecyclerView.adapter = drinkCardAdapter

        val root: View = binding.root

        return root
    }

    private fun makeAction(action: DrinkAction) {
        if (action is DrinkAction.GotoDetail) {
            NavHostFragment.findNavController(requireParentFragment())
                .navigate(R.id.detailDrinkFragment, bundleOf().apply {
                    putString("name", action.beer.name)
                    putString("desc", action.beer.description)
                    putInt("preview", action.beer.img)
                    putString("cl", action.beer.cl.toString() + " cl")
                    putString("currentPage", "Home")
                })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
