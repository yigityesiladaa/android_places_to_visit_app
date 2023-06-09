package com.placestovisit.ui.addPlace

import android.net.Uri
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.placestovisit.R
import com.placestovisit.firebase.repositories.FirebaseDBRepository
import com.placestovisit.common.states.BaseState
import com.placestovisit.common.utils.Utils
import com.placestovisit.firebase.repositories.FirebaseStorageRepository
import com.placestovisit.models.Place

class AddPlaceViewModel : ViewModel() {
    private var _firebaseDBRepository = FirebaseDBRepository()
    private var _firebaseStorageRepository = FirebaseStorageRepository()
    private val _state by lazy { MutableLiveData<BaseState>(BaseState.Loading) }
    val state: LiveData<BaseState> = _state
    private var _context : FragmentActivity? = null
    var imageUrl = MutableLiveData<String>()

    fun setContext(context : FragmentActivity){
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

    fun uploadImage(imageUri : Uri){
        _firebaseStorageRepository.uploadImage(imageUri).addOnCompleteListener{task->
            if(task.isSuccessful){
                _firebaseStorageRepository.getDownloadUrl().addOnSuccessListener { task->
                    imageUrl.postValue(task.toString())
                }
            }
        }
    }

}