package co.develhope.chooseyouowncocktail_g2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import co.develhope.chooseyouowncocktail_g2.databinding.FragmentDetailDrinkPageBinding


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"
private const val ARG_PARAM4 = "param4"

class DetailDrinkFragment : Fragment() {

    private var _binding: FragmentDetailDrinkPageBinding? = null

    private val binding get() = _binding!!

    private var param1: String? = null
    private var param2: String? = null
    private var param3: String? = null
    private var param4: String? = null
    private var param5: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString("name")
            param2 = it.getString("desc")
            param3 = it.getString("preview")
            param4 = it.getString("cl")
            param5 = it.getString("currentPage")
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

        binding.title.text= param1
        binding.description.text= param2
        binding.preview.setImageResource(Integer.parseInt(param3))
        binding.cl.text = param4

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            when(param5){
                "Search" -> view.findNavController().navigate(R.id.action_detailDrinkFragment_to_navigation_search)
                "Home" -> view.findNavController().navigate(R.id.action_detailDrinkFragment_to_navigation_home)
            }
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

