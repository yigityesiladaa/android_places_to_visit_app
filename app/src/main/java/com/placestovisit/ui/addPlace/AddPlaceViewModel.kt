package com.placestovisit.ui.addPlace

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.placestovisit.R
import com.placestovisit.firebase.repositories.FirebaseDBRepository
import com.placestovisit.common.states.BaseState
import com.placestovisit.common.utils.Utils
import com.placestovisit.models.Place

class AddPlaceViewModel : ViewModel() {
    private var _firebaseDBRepository = FirebaseDBRepository()
    private val _state by lazy { MutableLiveData<BaseState>(BaseState.Loading) }
    val state: LiveData<BaseState> = _state
    private var _context : Context? = null

    fun setContext(context : Context){
        _context = context
    }

    fun insert(place : Place){
        _firebaseDBRepository.insert(place)?.addOnCompleteListener { task->
            Utils.checkStatus<Void>(_state,task,_context?.getString(R.string.place_added_successfully_text))
        }
    }

    fun getFID() : String?{
        return _firebaseDBRepository.getFID()
    }
}