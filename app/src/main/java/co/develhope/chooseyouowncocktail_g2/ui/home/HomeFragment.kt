package co.develhope.chooseyouowncocktail_g2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.develhope.chooseyouowncocktail_g2.MainActivity
import co.develhope.chooseyouowncocktail_g2.R
import co.develhope.chooseyouowncocktail_g2.databinding.FragmentHomeBinding
import co.develhope.chooseyouowncocktail_g2.model.DBEvent
import co.develhope.chooseyouowncocktail_g2.model.DBResult
import co.develhope.chooseyouowncocktail_g2.model.DBViewModel
import com.google.android.material.snackbar.Snackbar


class HomeFragment : Fragment() {

    private lateinit var viewModel: DBViewModel

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        viewModel =
            (activity as MainActivity).mainViewModelFactory.create(DBViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        viewModel.send(DBEvent.RetrieveDrinksRandom)


        observer()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textHome.setOnClickListener {
            findNavController().navigate(R.id.detailDrinkFragment, null)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observer() {
        viewModel.result.observe(viewLifecycleOwner) {
            when (it) {
                is DBResult.Result -> it.db.forEach { println(it.name) }
                is DBResult.Error -> Snackbar.make(
                    binding.root,
                    "Error retrieving Drinks: $it",
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction("Retry") { viewModel.send(DBEvent.RetrieveDrinksRandom) }
                    .show()
            }
        }
    }

}