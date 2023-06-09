package com.placestovisit.firebase.repositories

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.placestovisit.firebase.services.IFirebaseStorageService

class FirebaseStorageRepository : IFirebaseStorageService{

    private val storageReference = getStorageReference()

    companion object {
        const val IMAGE_REQUEST_CODE = 1
        private var currentUserId = FirebaseAuthRepository.getCurrentUserId()
        private val IMAGE_PATH_NAME = "PlaceImages/$currentUserId/${System.currentTimeMillis()}"

        fun getStorageReference() : StorageReference {
            return FirebaseStorage.getInstance().getReference(IMAGE_PATH_NAME)
        }
    }

    override fun uploadImage(imageUri : Uri) : UploadTask {
        return storageReference.putFile(imageUri)
    }

    override fun getDownloadUrl(): Task<Uri> {
        return storageReference.downloadUrl
    }

}