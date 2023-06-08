package com.placestovisit.firebase.repositories

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.placestovisit.common.utils.Utils
import com.placestovisit.firebase.services.IFirebaseService
import com.placestovisit.models.Place

class FirebaseDBRepository : IFirebaseService<Place> {

    companion object{
        private var DATABASE_REFERENCE : DatabaseReference? = null

        private const val COLLECTION_NAME = "places"

        fun getFirebaseDBReference() : DatabaseReference {
            return DATABASE_REFERENCE ?: synchronized(this){
                val databaseRef= FirebaseDatabase.getInstance().getReference(COLLECTION_NAME)
                DATABASE_REFERENCE = databaseRef
                databaseRef
            }
        }
    }

    override fun insert(place: Place): Task<Void>? {
        val dbRef = getFirebaseDBReference()
        Utils.currentUserId?.let {
            return dbRef.child(it).child(place.id).setValue(place)
        }
        return null
    }

    override fun getAll() : Task<DataSnapshot>?{
        val dbRef = getFirebaseDBReference()
        Utils.currentUserId?.let {
            return dbRef.child(it).get()
        }
        return null
    }

    override fun remove(id: String): Task<Void>? {
        val dbRef = getFirebaseDBReference()
        Utils.currentUserId?.let {
            return dbRef.child(it).child(id).removeValue()
        }
        return null
    }

    fun getFID() : String?{
        return getFirebaseDBReference().push().key
    }


}



























