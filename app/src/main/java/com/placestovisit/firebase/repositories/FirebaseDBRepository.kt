package com.placestovisit.firebase.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.placestovisit.firebase.services.IFirebaseService
import com.placestovisit.models.Place

class FirebaseDBRepository : IFirebaseService<Place> {

    companion object{
        private const val COLLECTION_NAME = "places"

        fun getFirebaseDBReference() : DatabaseReference {
            return FirebaseDatabase.getInstance().getReference(COLLECTION_NAME)
        }
    }

    override fun insert(place: Place): Task<Void>? {
        val dbRef = getFirebaseDBReference()
        FirebaseAuthRepository.getCurrentUserId()?.let {
            return dbRef.child(it).child(place.id).setValue(place)
        }

        return null
    }

    override fun getAll() : Task<DataSnapshot>?{
        val dbRef = getFirebaseDBReference()
        FirebaseAuthRepository.getCurrentUserId()?.let {
            return dbRef.child(it).orderByChild("createdAt").get()
        }
        return null
    }

    override fun remove(id: String): Task<Void>? {
        val dbRef = getFirebaseDBReference()
        FirebaseAuthRepository.getCurrentUserId()?.let {
            return dbRef.child(it).child(id).removeValue()
        }
        return null
    }

    fun getFID() : String?{
        return getFirebaseDBReference().push().key
    }


}



























