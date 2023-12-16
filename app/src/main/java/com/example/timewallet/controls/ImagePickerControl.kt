package com.example.timewallet.controls

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class ImagePickerControl(private val fragment: Fragment) {
    private val PICK_IMAGE_REQUEST = 1

    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"  // Nur Bilder erlauben
        fragment.startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun saveImage(bitmap: Bitmap): File? {
        val directory = File(fragment.requireContext().filesDir, "images")

        if (!directory.exists()) {
            directory.mkdirs()
        }

        // Vorherige Datei löschen, falls vorhanden
        val previousImageFile = File(directory, "current_image.jpg")
        if (previousImageFile.exists()) {
            if (!previousImageFile.delete()) {
                Log.e("ImagePickerControl", "Konnte vorheriges Bild nicht löschen")
            }
        }

        val imageFile = File(directory, "current_image.jpg")

        try {
            val stream: OutputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
            // Hier können Sie die Datei nach Bedarf weiterverarbeiten oder in der App anzeigen
            return imageFile
        } catch (e: IOException) {
            Log.e("ImagePickerControl", "IOException beim Speichern des Bildes", e)
            return null
        } catch (e: Exception) {
            Log.e("ImagePickerControl", "Unbekannter Fehler beim Speichern des Bildes", e)
            return null
        }
    }

    fun getImageFile(): File? {
        val directory = File(fragment.requireContext().filesDir, "images")
        return if (directory.exists()) {
            File(directory, "current_image.jpg")
        } else {
            null
        }
    }


    fun handleActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        imageView: ImageView
    ) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            val bitmap =
                MediaStore.Images.Media.getBitmap(
                    fragment.requireActivity().contentResolver,
                    selectedImageUri
                )
            imageView.setImageBitmap(bitmap)

            // Hier wird das ausgewählte Bild gespeichert
            saveImage(bitmap)
        }
    }
}

