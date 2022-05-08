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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param_name = it.getString("name")
            param_desc = it.getString("desc")
            param_preview = it.getInt("preview")
            param_cl = it.getString("cl")
            param_currentPage = it.getString("currentPage")
        }
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

        binding.title.text = param_name
        binding.description.text = param_desc
        binding.preview.setImageResource(param_preview)
        binding.cl.text = param_cl

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

