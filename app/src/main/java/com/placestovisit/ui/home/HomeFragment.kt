package com.placestovisit.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.placestovisit.R
import com.placestovisit.common.extensions.showAlertDialog
import com.placestovisit.common.utils.Utils
import com.placestovisit.databinding.FragmentHomeBinding
import com.placestovisit.firebase.repositories.FirebaseAuthRepository
import com.placestovisit.models.Place

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var _homeViewModel: HomeViewModel
    private lateinit var _navController: NavController
    private lateinit var _customPlacesAdapter: CustomPlacesAdapter
    private var _places = mutableListOf<Place>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        registerEvents()
        listenEvents()
    }

    private fun init(view: View) {
        _navController = Navigation.findNavController(view)
        _homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _homeViewModel.setContext(requireContext())
        _customPlacesAdapter = CustomPlacesAdapter(requireContext())
        binding.placesListView.adapter = _customPlacesAdapter
        _homeViewModel.getAllPlaces()
    }

    private fun registerEvents() {
        binding.customToolbar.btnSignOutImage.setOnClickListener {
            _homeViewModel.signOut()
        }
        binding.floatingActionButton.setOnClickListener {
            _navController.navigate(R.id.action_homeFragment_to_addPlaceFragment)
        }
        binding.placesListView.onItemLongClickListener = placeListViewItemLongClickListener
    }

    private val placeListViewItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
        showAlertDialog(
            getString(R.string.delete_place_text),
            getString(R.string.delete_pop_up_title_text),
            getString(R.string.delete_button_text),
            getString(R.string.cancel_button_text),
        ) { dialogInterface, i ->
            val placeId = _places[position].id
            _places.remove(_places[position])
            _homeViewModel.remove(placeId)
            _customPlacesAdapter.submitList(_places)
        }
        true
    }

    private fun onLoading(){
        binding.progressBar.visibility = View.VISIBLE
        binding.placesListView.visibility = View.GONE
    }

    private fun checkListIsEmpty(){
        if(_places.isEmpty()){
            binding.txtNothingToShowYet.visibility = View.VISIBLE
            binding.placesListView.visibility = View.GONE
        }else{
            binding.txtNothingToShowYet.visibility = View.GONE
            binding.placesListView.visibility = View.VISIBLE
        }
    }

    private fun listenEvents() {
        _homeViewModel.state.observe(viewLifecycleOwner) { baseState ->
            Utils.checkState(activity, baseState,onLoading()){
                binding.progressBar.visibility = View.GONE
                checkListIsEmpty()
            }
            FirebaseAuthRepository.getCurrentUserId()?.let { userId->
                if (userId.isEmpty()) {
                    _navController.navigate(R.id.action_homeFragment_to_signInFragment)
                }
            }
        }
        _homeViewModel.places.observe(viewLifecycleOwner) { placeList ->
            _places = placeList
            _customPlacesAdapter.submitList(placeList)
        }
    }

    override fun onStart() {
        super.onStart()
        _homeViewModel.getAllPlaces()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}