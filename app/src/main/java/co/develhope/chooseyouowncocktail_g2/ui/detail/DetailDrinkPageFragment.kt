package co.develhope.chooseyouowncocktail_g2.ui.detail

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import co.develhope.chooseyouowncocktail_g2.*
import co.develhope.chooseyouowncocktail_g2.adapter.DrinkAction
import co.develhope.chooseyouowncocktail_g2.databinding.FragmentDetailDrinkPageBinding
import co.develhope.chooseyouowncocktail_g2.usecase.model.Drink
import org.koin.android.ext.android.inject

const val DETAILPAGE_PREVIEW_SIZE = 300

class DetailDrinkFragment : Fragment() {

    private var _binding: FragmentDetailDrinkPageBinding? = null

    private val binding get() = _binding!!

    private lateinit var backPressedCallback: OnBackPressedCallback


    private lateinit var drinkAction: (DrinkAction) -> Unit

    private val viewModel: DetailViewModel by inject()

    companion object {
        @JvmStatic
        fun newInstance(drink: Drink?, action: (DrinkAction) -> Unit) =
            DetailDrinkFragment().apply {
                if (drink != null) {
                    viewModel.initDetailPage(drink)
                    drinkAction = action
                }
                return this
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MainActivity).menuInflater.inflate(R.menu.topbarbutton, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.backbutton -> {
                (activity as MainActivity).supportFragmentManager.findFragmentByTag("DetailDrinkFragment")
                    ?.let {
                        (activity as MainActivity).remove(it)
                    }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailDrinkPageBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
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

            if (!drink.img.isNullOrEmpty()) {
                binding.preview.setImageByUrl(
                    drink.img,
                    DETAILPAGE_PREVIEW_SIZE,
                    DETAILPAGE_PREVIEW_SIZE
                )
            }

            binding.buttonFavorite.setBackgroundResource(showFavoriteStatus(drink))

            binding.buttonFavorite.setOnClickListener {
                drinkAction(DrinkAction.SetPref(drink, !drink.favourite))
                viewModel.updateDetailedDrink(drink)
            }

            binding.ingredients.text =
                String.format(
                    "${resources.getString(R.string.ingredients)}: %s",
                    drink.ingredients.toString().replace("[", "")
                        .replace("]", "")
                )

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
