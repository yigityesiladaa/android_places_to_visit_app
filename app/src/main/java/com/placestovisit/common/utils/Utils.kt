package com.placestovisit.common.utils

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.placestovisit.common.states.BaseState

class Utils {
    companion object {

        fun checkState(
            activity: FragmentActivity?,
            state: BaseState,
            onLoading: Unit? = null,
            onSuccess: (() -> Unit)? = null,
        ) {
            when (state) {
                is BaseState.Loading -> {onLoading}
                is BaseState.Success -> {
                    if(state.message != null){
                        Toast.makeText(
                            activity?.applicationContext,
                            state.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    if(onSuccess != null) onSuccess()
                }
                is BaseState.Error -> {
                    Toast.makeText(
                        activity?.applicationContext,
                        state.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        fun <T> checkStatus(
            state: MutableLiveData<BaseState>,
            task: Task<T>,
            successMessage: String? = null,
            onSuccess: (() -> Unit)? = null
        ) {
            if (task.isSuccessful) {
                state.postValue(BaseState.Success(successMessage))
                if (onSuccess != null) onSuccess()
            } else {
                task.exception?.let { exception ->
                    state.postValue(BaseState.Error(exception.localizedMessage))
                }
            }
        }


    }
}