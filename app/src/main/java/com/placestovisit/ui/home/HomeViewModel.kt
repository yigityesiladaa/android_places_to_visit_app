package com.placestovisit.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.placestovisit.R
import com.placestovisit.common.utils.Utils
import com.placestovisit.firebase.repositories.FirebaseAuthRepository
import com.placestovisit.firebase.repositories.FirebaseDBRepository
import com.placestovisit.common.states.BaseState
import com.placestovisit.models.Place

class HomeViewModel : ViewModel() {
    private var _firebaseAuthRepository = FirebaseAuthRepository()
    private var _firebaseDbRepository = FirebaseDBRepository()
    private val _state by lazy { MutableLiveData<BaseState>(BaseState.Loading) }
    val state: LiveData<BaseState> = _state
    var places = MutableLiveData<MutableList<Place>>()
    private var _context : Context? = null

    fun setContext(context : Context){
        _context = context
    }

    fun getAllPlaces() {
        _firebaseDbRepository.getAll()?.addOnCompleteListener { task ->
            Utils.checkStatus(_state, task) {
                places.value = mutableListOf()
                task.result.children.forEach { ds ->
                    val place = ds.getValue(Place::class.java)
                    place?.let {
                        if (places.value != null) places.value!!.add(0,it) else places.value =
                            mutableListOf(it)
                    }
                }
            }
        }
    }

    fun remove(id: String) {
        _firebaseDbRepository.remove(id)?.addOnCompleteListener { task ->
            Utils.checkStatus(_state, task, _context?.getString(R.string.place_deleted_successfully_text))
        }
    }

    fun signOut() {
        _firebaseAuthRepository.signOut()
        _state.postValue(BaseState.Success(_context?.getString(R.string.sign_out_text)))
    }

}