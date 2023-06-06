package com.placestovisit.common.states

sealed class BaseState{
    object Loading : BaseState()
    class Success(val message : String? = null) : BaseState()
    class Error(val message: String? = null) : BaseState()
}

