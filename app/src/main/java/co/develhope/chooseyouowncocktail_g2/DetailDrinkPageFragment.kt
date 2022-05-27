package co.develhope.chooseyouowncocktail_g2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import co.develhope.chooseyouowncocktail_g2.databinding.FragmentDetailDrinkPageBinding
import co.develhope.chooseyouowncocktail_g2.model.Beer


const val param_drink_ID = "drink_ID"

class DetailDrinkFragment : Fragment() {

    private var _binding: FragmentDetailDrinkPageBinding? = null

    private val binding get() = _binding!!


    companion object {
        @JvmStatic
        val fragmentTag = DetailDrinkFragment::class.java.canonicalName
            ?: "DetailDrinkFragment"
        fun newInstance(drinkID: Int) = DetailDrinkFragment().apply {
            arguments = Bundle().apply {
                putInt(param_drink_ID, drinkID)
            }
            return this
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  requireActivity().actionBar?.displayOptions
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailDrinkPageBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            DrinkList.getByID(it.getInt(param_drink_ID))?.let {
               inflateUI(it)
            }?: run {
                Toast.makeText(context, "Nothing Found", Toast.LENGTH_LONG).show()
                (activity as MainActivity).remove(this)
            }
        }


        var switch: Boolean = false
        var bf = binding.buttonFavorite
        val imOn = requireActivity().getResources()
            .getIdentifier("ic_fav_off", "drawable", requireContext().getPackageName())
        val imOff = requireActivity().getResources()
            .getIdentifier("ic_fav_on", "drawable", requireContext().getPackageName())
        bf.setOnClickListener {
            if (!switch) {
                bf.setBackgroundResource(imOn)
                switch = true
            } else {
                bf.setBackgroundResource(imOff)
                switch = false
            }
        }
    }


    private fun inflateUI(drink : Beer){
        binding.title.text = drink.name
        binding.description.text = drink.description
        binding.preview.setImageByUrl(
            "https://www.thecocktaildb.com/images/media/drink/metwgh1606770327.jpg",
            200,
            200
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

