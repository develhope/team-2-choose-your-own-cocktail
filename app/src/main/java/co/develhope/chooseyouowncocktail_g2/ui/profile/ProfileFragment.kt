package co.develhope.chooseyouowncocktail_g2.ui.profile

import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import co.develhope.chooseyouowncocktail_g2.User
import co.develhope.chooseyouowncocktail_g2.databinding.FragmentProfileBinding
import org.koin.android.ext.android.inject


const val RESULT_LOAD_IMAGE = 1

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var profilePicUri = ""

    private val viewModel: ProfileViewModel by inject()

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
            saveLocalUser()
        }
        binding.logOut.setOnClickListener {
            resetLocalUser()
        }

        if (viewModel.isUserInitialized()) {
            setupUserData()
        }

        binding.imageView.setOnClickListener {
            pickImageFromStorage()
        }

        binding.pickImage.setOnClickListener {
            pickImageFromStorage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RESULT_LOAD_IMAGE && data != null) {
            profilePicUri = data.data.toString()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                data.data?.let {
                    context!!.contentResolver.takePersistableUriPermission(
                        it,
                        FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                }
            }
            binding.imageView.setImageURI(data.data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun setupUserData() {
        val user = viewModel.user
        binding.imageView.setImageURI(user.profilePic.toUri())
        binding.nameSurname.setText(user.name)
        binding.stringDateOfBirth.setText(user.birth)
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
                binding.stringDateOfBirth.text.toString(),
                binding.genderSwitch.isChecked,
                binding.stringTelephoneNumber.text.toString(),
                binding.stringEmail.text.toString()
            )
        )
    }

    private fun pickImageFromStorage(){
        startActivityForResult(
            Intent(
                Intent.ACTION_OPEN_DOCUMENT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ), RESULT_LOAD_IMAGE
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
        binding.stringDateOfBirth.setText("")
        binding.stringEmail.setText("")
        binding.genderSwitch.switchPadding = 0
        binding.stringTelephoneNumber.setText("")

    }

}