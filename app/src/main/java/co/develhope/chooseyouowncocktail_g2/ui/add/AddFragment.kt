package co.develhope.chooseyouowncocktail_g2.ui.add

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import co.develhope.chooseyouowncocktail_g2.MainActivity
import co.develhope.chooseyouowncocktail_g2.R
import co.develhope.chooseyouowncocktail_g2.databinding.FragmentAddBinding
import co.develhope.chooseyouowncocktail_g2.usecase.model.Drink
import org.koin.android.ext.android.inject


const val ORDINARYDRINK = "Ordinary Drink"
const val COCKTAIL = "Cocktail"
const val SHAKE = "Shake"
const val OTHERUNKNOWN = "Other / Unknown"
const val COCOA = "Cocoa"
const val SHOT = "Shot"
const val COFFEETEA = "Coffee / Tea"
const val LIQUEUR = "Liqueur"
const val PUNCH = "Punch / Party Drink"
const val BEER = "Beer"
const val SOFTDRINK = "Soft Drink"



class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private var drinkPicUri = ""

    private val viewModel: AddViewModel by inject()

    companion object {
        @JvmStatic
        fun newInstance() =
            AddFragment().apply {
                return this
            }
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                drinkPicUri = uri.toString()

                binding.imageView.setImageURI(uri)
            }
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        onBackPressCloseFrag()
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.inputCategory.setOnClickListener {
            chooseCategory()
        }

        binding.save.setOnClickListener {
            viewModel.saveLocalDrink(
                Drink(
                    0,
                    binding.inputName.text.toString(),
                    binding.inputLongdesc.text.toString(),
                    binding.inputShortdesc.text.toString(),
                    binding.inputCategory.text.toString(),
                   listOf(binding.inputIngredients.text.toString()),
                    drinkPicUri,
                    false
                )
            )
            println(binding.inputCategory.text.toString())
            Toast.makeText(context, R.string.saved, Toast.LENGTH_LONG).show()
            (activity as MainActivity).remove(this)
        }

        binding.imageView.setOnClickListener {
            try {
                getContent.launch("image/*")
            } catch (e: SecurityException) {
                println(e.message)
            }
        }

        binding.pickImage.setOnClickListener {
            try {
                getContent.launch("image/*")
            } catch (e: SecurityException) {
                println(e.message)
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun chooseCategory() {
        var choosedCategory = ""
        val builder = AlertDialog.Builder((activity as MainActivity))
        builder.setTitle("Choose Category")

        builder.setItems(
            arrayOf(
                ORDINARYDRINK,
                COCKTAIL,
                SHAKE,
                OTHERUNKNOWN,
                COCOA,
                SHOT,
                COFFEETEA,
                LIQUEUR,
                PUNCH,
                BEER,
                SOFTDRINK
            )
        ) { dialog, which ->
            when (which) {
                0 -> {
                    choosedCategory = ORDINARYDRINK
                }
                1 -> {
                    choosedCategory = COCKTAIL
                }
                2 -> {
                    choosedCategory = SHAKE
                }
                3 -> {
                    choosedCategory = OTHERUNKNOWN
                }
                4 -> {
                    choosedCategory = COCOA
                }
                5 -> {
                    choosedCategory = SHOT
                }
                6 -> {
                    choosedCategory = COFFEETEA
                }
                7 -> {
                    choosedCategory = LIQUEUR
                }
                8 -> {
                    choosedCategory = PUNCH
                }
                9 -> {
                    choosedCategory = BEER
                }
                10 -> {
                    choosedCategory = SOFTDRINK
                }

            }
            binding.inputCategory.text = choosedCategory
        }
        builder.create().show()
    }


    private fun onBackPressCloseFrag(): OnBackPressedCallback {
        return requireActivity().onBackPressedDispatcher.addCallback {
            (activity as MainActivity).remove(this@AddFragment)
        }
    }


}
