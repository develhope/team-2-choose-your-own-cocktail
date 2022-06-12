package co.develhope.chooseyouowncocktail_g2.ui.home

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import co.develhope.chooseyouowncocktail_g2.DrinkList
import co.develhope.chooseyouowncocktail_g2.DrinkList.setList
import co.develhope.chooseyouowncocktail_g2.MainActivity
import co.develhope.chooseyouowncocktail_g2.databinding.FragmentHomeBinding
import co.develhope.chooseyouowncocktail_g2.domain.DBEvent
import co.develhope.chooseyouowncocktail_g2.domain.DBResult
import co.develhope.chooseyouowncocktail_g2.domain.DBViewModel
import com.google.android.material.snackbar.Snackbar
import co.develhope.chooseyouowncocktail_g2.DrinkAction

import co.develhope.chooseyouowncocktail_g2.adapter.DrinkCardAdapter
import co.develhope.chooseyouowncocktail_g2.ui.DetailDrinkFragment

class HomeFragment : Fragment() {

    private lateinit var viewModel: DBViewModel

    private var _binding: FragmentHomeBinding? = null

    private val currentLetter = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var drinkCardAdapter: DrinkCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        viewModel =
            (activity as MainActivity).mainViewModelFactory.create(DBViewModel::class.java)


        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        viewModel.send(DBEvent.RetrieveDrinksByFirstLetter(currentLetter[0]))

        observer()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        

        val drinkCardAdapter = DrinkCardAdapter(
            DrinkList.drinkList(),
        ) { action -> makeActionDone(action) }
        //binding.cciao.visibility=Visibility(View.VISIBLE)

        binding.drinkCardRecyclerView.adapter = drinkCardAdapter

    }


    private fun observer() {
        viewModel.result.observe(viewLifecycleOwner) {
            when (it) {
                is DBResult.Result -> {
                    it.db.setList()
                    println(DrinkList.drinkList().size)
                    DrinkList.drinkList().forEach {
                        println(it.name)
                    }
                    inflateDrinkList()
                    println(drinkCardAdapter.itemCount)
                }
                is DBResult.Error -> Snackbar.make(
                    binding.root,
                    "Error retrieving Drinks: $it",
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction("Retry") {
                        viewModel.send(
                            DBEvent.RetrieveDrinksByFirstLetter(
                                currentLetter[0]
                            )
                        )
                    }
                    .show()
            }
        }
    }

}


