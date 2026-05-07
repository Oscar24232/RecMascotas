package com.example.trabajoredsocial.ViewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import java.io.File
import java.net.URL
import com.google.firebase.storage.FirebaseStorage
class ImagenViewModel: ViewModel() {
    val TAG = "Oscar"
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference.child("imagenes")
    private val _urlPfp = MutableLiveData<URL?>()
    val urlPfp: LiveData<URL?> = _urlPfp

    private val _imageUri = MutableLiveData<Uri>(Uri.EMPTY)
    val imageUri: LiveData<Uri> get() = _imageUri

    private val _imageFile = MutableLiveData<File?>()
    val imageFile: LiveData<File?> get() = _imageFile

    private val _isUploading = MutableLiveData<Boolean>(false)
    val isUploading: LiveData<Boolean> get() = _isUploading

    private val _uploadSuccess = MutableLiveData<Boolean>()
    val uploadSuccess: LiveData<Boolean> get() = _uploadSuccess

    private val _fileList = MutableLiveData<List<String>>(emptyList())
    val fileList: LiveData<List<String>> get() = _fileList


    fun updateImageUri(uri: Uri) {
        _imageUri.value = uri
    }

    fun setImageFile(file: File) {
        _imageFile.value = file
    }

    fun setUrlPfp(url: URL) {
        this._urlPfp.value = url
    }
    fun uploadImage(context: Context) {
        val file = _imageFile.value ?: return
        val fileUri = Uri.fromFile(file)
        val ref = storageRef.child("productos/${fileUri.lastPathSegment}")

        _isUploading.value = true

        ref.putFile(fileUri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    _isUploading.value = false
                    _uploadSuccess.value = true

                    setUrlPfp(URL(uri.toString()))

                    Toast.makeText(
                        context,
                        "Imagen subida correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d(TAG, "URL real: $uri")
                }
            }
            .addOnFailureListener { exception ->
                _isUploading.value = false
                _uploadSuccess.value = false

                Toast.makeText(
                    context,
                    "Error: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}