package co.develhope.chooseyouowncocktail_g2.ui.profile

import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import co.develhope.chooseyouowncocktail_g2.R
import co.develhope.chooseyouowncocktail_g2.User
import co.develhope.chooseyouowncocktail_g2.databinding.FragmentProfileBinding
import org.koin.android.ext.android.inject


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var profilePicUri = ""

    private val viewModel: ProfileViewModel by inject()

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                profilePicUri = uri.toString()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    context!!.contentResolver.takePersistableUriPermission(
                        uri,
                        FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                }
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
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.save.setOnClickListener {
            Toast.makeText(context, R.string.saved, Toast.LENGTH_LONG).show()
            saveLocalUser()
        }
        binding.logOut.setOnClickListener {
            resetLocalUser()
        }

        if (viewModel.isUserInitialized()) {
            setupUserData()
        }

        binding.imageView.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.pickImage.setOnClickListener {
            getContent.launch("image/*")
        }
    }


    private fun setupUserData() {
        val user = viewModel.user
        binding.imageView.setImageURI(user.profilePic.toUri())
        binding.nameSurname.setText(user.name)
        binding.stringDateofbirth.setText(user.birth)
        binding.stringEmail.setText(user.email)
        binding.genderSwitch.isChecked = user.gender
        binding.stringTelephoneNumber.setText(user.tel)
    }

    private fun saveLocalUser() {
        println(binding.genderSwitch.isChecked)
        viewModel.saveLocalUser(
            User(
                profilePicUri,
                binding.nameSurname.text.toString(),
                binding.stringDateofbirth.text.toString(),
                binding.genderSwitch.isChecked,
                binding.stringTelephoneNumber.text.toString(),
                binding.stringEmail.text.toString()
            )
        )
    }

    private fun resetLocalUser() {
        viewModel.saveLocalUser(
            User(
                "",
                "",
                "",
                false,
                "",
                ""
            )
        )
        binding.imageView.setImageURI(null)
        binding.nameSurname.setText("")
        binding.stringDateofbirth.setText("")
        binding.stringEmail.setText("")
        binding.genderSwitch.switchPadding = 0
        binding.stringTelephoneNumber.setText("")

    }

}