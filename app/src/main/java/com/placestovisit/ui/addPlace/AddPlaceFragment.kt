package com.placestovisit.ui.addPlace

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.placestovisit.common.extensions.showToast
import com.placestovisit.common.utils.Utils
import com.placestovisit.firebase.repositories.FirebaseStorageRepository
import com.placestovisit.models.Place
import com.placestovisit.R
import com.placestovisit.databinding.FragmentAddPlaceBinding

@RequiresApi(Build.VERSION_CODES.O)
class AddPlaceFragment : Fragment() {

    private var _binding: FragmentAddPlaceBinding? = null
    private val binding get() = _binding!!
    private lateinit var _addPlaceViewModel: AddPlaceViewModel
    private lateinit var pickedImage: Uri
    private var imageUrl = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        _binding = FragmentAddPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        registerEvents()
        listenEvents()

    }

    private fun init() {
        _addPlaceViewModel = ViewModelProvider(this)[AddPlaceViewModel::class.java]
        _addPlaceViewModel.setContext(requireActivity())
    }

    private fun registerEvents() {
        binding.customToolbar.btnBackImage.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.customToolbar.btnSaveImage.setOnClickListener(btnSaveClickListener)

        binding.btnSelectImage.setOnClickListener {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        Glide.with(requireContext()).load("").into(binding.imgPlaceImage)
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, FirebaseStorageRepository.IMAGE_REQUEST_CODE)
        binding.imageProgressBar.visibility = View.VISIBLE
    }

    private val btnSaveClickListener = View.OnClickListener {
        val title = binding.etTitle.text.toString().trim()
        val city = binding.etCity.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()

        val fid = _addPlaceViewModel.getFID()
        if (fid != null) {
            if (title.isNotEmpty() && city.isNotEmpty() && description.isNotEmpty() && imageUrl != "") {
                val place = Place(fid, title, city, description, true,imageUrl)
                _addPlaceViewModel.insert(place)
            } else {
                showToast(getString(R.string.all_fields_required_text))
            }
        } else {
            showToast(getString(R.string.fid_error_text))
        }
    }


    private fun listenEvents() {
        _addPlaceViewModel.state.observe(viewLifecycleOwner) { baseState ->
            Utils.checkState(
                activity,
                baseState,
                null,
            ) {
                findNavController().popBackStack()
            }
        }

        _addPlaceViewModel.imageUrl.observe(viewLifecycleOwner){
            imageUrl = it
            binding.imageProgressBar.visibility = View.GONE
            Glide.with(requireContext()).load(it).into(binding.imgPlaceImage)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null) {
            if (data.data != null) {
                pickedImage = data.data!!
                _addPlaceViewModel.uploadImage(pickedImage)
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}