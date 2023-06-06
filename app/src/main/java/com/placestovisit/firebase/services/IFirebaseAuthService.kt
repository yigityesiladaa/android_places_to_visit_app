package com.placestovisit.firebase.services

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

interface IFirebaseAuthService {

    fun signIn(email : String, password : String) : Task<AuthResult>

    fun signUp(email : String, password : String) : Task<AuthResult>

    fun signOut()

    fun resetPassword(email : String) : Task<Void>

}