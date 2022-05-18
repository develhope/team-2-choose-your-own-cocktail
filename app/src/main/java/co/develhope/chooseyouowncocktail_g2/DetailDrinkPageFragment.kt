package co.develhope.chooseyouowncocktail_g2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import co.develhope.chooseyouowncocktail_g2.databinding.FragmentDetailDrinkPageBinding
import co.develhope.chooseyouowncocktail_g2.model.Beer


const val param_drink_ID = "drink_ID"

class DetailDrinkFragment : Fragment() {

    private var _binding: FragmentDetailDrinkPageBinding? = null

    private val binding get() = _binding!!

    private var param_currentPage: String? = null

    companion object {
        @JvmStatic
        fun newInstance(drinkID: Int) = DetailDrinkFragment().apply {
            val TAG = DetailDrinkFragment::class.java.canonicalName
                ?: "DetailDrinkFragment"
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
            val beer = DrinkList.getByID(it.getInt(param_drink_ID))
            if(beer!=null) {
                binding.title.text = beer.name
                binding.description.text = beer.description
                binding.cl.text = beer.cl.toString()
            }
        }


        var switch:Boolean= false
        var bf=binding.buttonFavorite
        val imOn= requireActivity().getResources().getIdentifier("ic_fav_off","drawable", requireContext().getPackageName())
        val imOff= requireActivity().getResources().getIdentifier("ic_fav_on","drawable", requireContext().getPackageName())
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

