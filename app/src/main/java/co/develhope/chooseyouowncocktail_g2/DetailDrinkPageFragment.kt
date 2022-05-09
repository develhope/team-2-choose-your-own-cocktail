package co.develhope.chooseyouowncocktail_g2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import co.develhope.chooseyouowncocktail_g2.databinding.FragmentDetailDrinkPageBinding


class DetailDrinkFragment : Fragment() {

    private var _binding: FragmentDetailDrinkPageBinding? = null

    private val binding get() = _binding!!

    private var param_name: String? = null
    private var param_desc: String? = null
    private var param_preview: Int = 0
    private var param_cl: String? = null
    private var param_currentPage: String? = null
    private var param_favorite: Boolean? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //getting bundle param
        arguments?.let {
            param_name = it.getString("name")
            param_desc = it.getString("desc")
            param_preview = it.getInt("preview")
            param_cl = it.getString("cl")
            param_currentPage = it.getString("currentPage")
            param_favorite = it.getBoolean("favorite")
        }
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

        val preferiteImageOn= requireActivity().resources.getIdentifier("ic_fav_on","drawable", requireContext().packageName)
        val preferiteImageOff= requireActivity().resources.getIdentifier("ic_fav_off","drawable", requireContext().packageName)

        binding.title.text = param_name
        binding.description.text = param_desc
        binding.preview.setImageResource(param_preview)
        binding.cl.text = param_cl
        if (param_favorite!!)
            binding.buttonFavorite.setBackgroundResource(preferiteImageOn)
        else
            binding.buttonFavorite.setBackgroundResource(preferiteImageOff)

        binding.buttonFavorite.setOnClickListener {
            TODO()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            when (param_currentPage) {
                "Search" -> view.findNavController()
                    .navigate(R.id.action_detailDrinkFragment_to_navigation_search)
                "Home" -> view.findNavController()
                    .navigate(R.id.action_detailDrinkFragment_to_navigation_home)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

