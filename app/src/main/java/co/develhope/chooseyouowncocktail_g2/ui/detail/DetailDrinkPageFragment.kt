package co.develhope.chooseyouowncocktail_g2.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import co.develhope.chooseyouowncocktail_g2.*
import co.develhope.chooseyouowncocktail_g2.adapter.DrinkAction
import co.develhope.chooseyouowncocktail_g2.databinding.FragmentDetailDrinkPageBinding
import co.develhope.chooseyouowncocktail_g2.usecase.model.Drink

const val DETAILPAGE_PREVIEW_SIZE=300

class DetailDrinkFragment : Fragment() {

    private var _binding: FragmentDetailDrinkPageBinding? = null

    private val binding get() = _binding!!

    private lateinit var backPressedCallback: OnBackPressedCallback


    private lateinit var drinkAction: (DrinkAction) -> Unit

    private val viewModel =
        ViewModelFactory().create(DetailViewModel::class.java)

    companion object {
        @JvmStatic
        val fragmentTag = DetailDrinkFragment::class.java.canonicalName
            ?: "DetailDrinkFragment"

        fun newInstance(drink: Drink?, action: (DrinkAction) -> Unit) =
            DetailDrinkFragment().apply {
                if (drink != null) {
                    viewModel.updateDetailedDrink(drink)
                    drinkAction = action
                }
                return this
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailDrinkPageBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backPressedCallback = onBackPressCloseFrag()

        inflateUI()


    }


    private fun inflateUI() {
        viewModel.drink.observe(viewLifecycleOwner) { drink ->
            binding.title.text = drink.name
            binding.description.text = drink.description
            binding.cl.text = drink.category
            binding.preview.setImageByUrl(
                drink.img,
                DETAILPAGE_PREVIEW_SIZE,
                DETAILPAGE_PREVIEW_SIZE
            )

            binding.buttonFavorite.setBackgroundResource(showFavoriteStatus(drink))

            binding.buttonFavorite.setOnClickListener {
                drinkAction(DrinkAction.SetPref(drink, !drink.favourite))
                viewModel.updateDetailedDrink(drink)
            }

        }
    }


    private fun showFavoriteStatus(drink: Drink): Int {
        val icon = if (drink.favourite) {
            R.drawable.ic_fav_on
        } else {
            R.drawable.ic_fav_off
        }
        return icon
    }


    override fun onDestroyView() {
        super.onDestroyView()
        backPressedCallback.remove()
        _binding = null
    }


    private fun onBackPressCloseFrag(): OnBackPressedCallback {
        return requireActivity().onBackPressedDispatcher.addCallback {
            (activity as MainActivity).remove(this@DetailDrinkFragment)
        }
    }
}

