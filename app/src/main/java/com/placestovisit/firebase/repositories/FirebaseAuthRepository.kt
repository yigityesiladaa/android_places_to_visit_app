package com.placestovisit.firebase.repositories

import android.util.Patterns
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.placestovisit.firebase.services.IFirebaseAuthService

class FirebaseAuthRepository : IFirebaseAuthService{
    private val authInstance = getAuthInstance()

    companion object{
        fun getAuthInstance() : FirebaseAuth{
            return FirebaseAuth.getInstance()
        }

        fun getCurrentUserId() : String?{
            return getAuthInstance().uid
        }

    }

    fun isEmailValid(email : String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    override fun signIn(email: String, password: String): Task<AuthResult> {
        val authInstance = getAuthInstance()
        return authInstance.signInWithEmailAndPassword(email,password)
    }

    override fun signUp(email: String, password: String): Task<AuthResult> {
        val authInstance = getAuthInstance()
        return authInstance.createUserWithEmailAndPassword(email, password)
    }

    override fun signOut() {
        val authInstance = getAuthInstance()
        authInstance.signOut()
    }

    override fun resetPassword(email: String) : Task<Void> {
        val authInstance = getAuthInstance()
        return authInstance.sendPasswordResetEmail(email)
    }

}