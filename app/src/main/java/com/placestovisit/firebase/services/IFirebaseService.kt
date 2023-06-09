package com.placestovisit.firebase.services

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot

interface IFirebaseService<T> {

    fun getAll() : Task<DataSnapshot>?

    fun insert(data : T) : Task<Void>?

    fun remove(id : String) : Task<Void>?

}