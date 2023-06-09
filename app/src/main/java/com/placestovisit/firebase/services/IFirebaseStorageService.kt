package com.placestovisit.firebase.services

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask

interface IFirebaseStorageService {

    fun uploadImage(imageUri : Uri) : UploadTask

    fun getDownloadUrl() : Task<Uri>

}