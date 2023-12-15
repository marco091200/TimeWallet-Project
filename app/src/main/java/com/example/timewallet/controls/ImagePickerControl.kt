package com.example.timewallet.controls

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.widget.ImageView
import androidx.fragment.app.Fragment

class ImagePickerControl (private val fragment: Fragment){
    private val PICK_IMAGE_REQUEST = 1

    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        fragment.startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?, imageView: ImageView) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(fragment.requireActivity().contentResolver, selectedImageUri)
            imageView.setImageBitmap(bitmap)
        }
    }

}