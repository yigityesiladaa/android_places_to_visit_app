package com.placestovisit.ui.addPlace

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.placestovisit.R
import com.placestovisit.common.extensions.showToast
import com.placestovisit.common.utils.Utils
import com.placestovisit.databinding.FragmentAddPlaceBinding
import com.placestovisit.models.Place

class AddPlaceFragment : Fragment() {

    private var _binding : FragmentAddPlaceBinding? = null
    private val binding get() = _binding!!
    private lateinit var _addPlaceViewModel: AddPlaceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPlaceBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        registerEvents()
        listenEvents()
    }

    private fun init(){
        _addPlaceViewModel = ViewModelProvider(this)[AddPlaceViewModel::class.java]
        _addPlaceViewModel.setContext(requireContext())
    }

    private fun registerEvents(){
        binding.customToolbar.btnBackImage.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.customToolbar.btnSaveImage.setOnClickListener(btnSaveClickListener)
    }

    private val btnSaveClickListener = View.OnClickListener {
        val title = binding.etTitle.text.toString().trim()
        val city = binding.etCity.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()

        val fid = _addPlaceViewModel.getFID()
        if(fid != null){
            if(title.isNotEmpty() && city.isNotEmpty() && description.isNotEmpty()){
                val place = Place(fid,title,city,description)
                _addPlaceViewModel.insert(place)
            }else{
                showToast(getString(R.string.all_fields_required_text))
            }
        }else{
            showToast(getString(R.string.fid_error_text))
        }
    }

    private fun listenEvents(){
        _addPlaceViewModel.state.observe(viewLifecycleOwner){ baseState->
            Utils.checkState(
                activity,
                baseState,
                null,
            ){
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}