package com.example.timewallet.controls

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

@Suppress("DEPRECATION")
class ImagePickerControl(private val fragment: Fragment) {
    private val pickImageRequest = 1

    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        fragment.startActivityForResult(intent, pickImageRequest)
    }

    private fun saveImage(bitmap: Bitmap): File? {
        val directory = File(fragment.requireContext().filesDir, "images")

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val previousImageFile = File(directory, "current_image.jpg")
        if (previousImageFile.exists()) {
            if (!previousImageFile.delete()) {
                Log.e("ImagePickerControl", "Konnte vorheriges Bild nicht löschen")
            }
        }

        val imageFile = File(directory, "current_image.jpg")

        return try {
            val maxWidth = 720
            val maxHeight = 1280

            val width = bitmap.width
            val height = bitmap.height

            val scale = (maxWidth.toFloat() / width).coerceAtMost(maxHeight.toFloat() / height)

            val matrix = Matrix()
            matrix.postScale(scale, scale)

            val resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)

            val stream: OutputStream = FileOutputStream(imageFile)
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()

            imageFile
        } catch (e: IOException) {
            Log.e("ImagePickerControl", "IOException beim Speichern des Bildes", e)
            null
        } catch (e: Exception) {
            Log.e("ImagePickerControl", "Unbekannter Fehler beim Speichern des Bildes", e)
            null
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
        if (requestCode == pickImageRequest && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            val bitmap =
                MediaStore.Images.Media.getBitmap(
                    fragment.requireActivity().contentResolver,
                    selectedImageUri
                )
            imageView.setImageBitmap(bitmap)
            saveImage(bitmap)
        }
    }

    fun deleteImageFolder() {
        val directory = File(fragment.requireContext().filesDir, "images")

        if (directory.exists()) {
            val files = directory.listFiles()
            if (files != null) {
                for (file in files) {
                    if (!file.delete()) {
                        Log.e("ImagePickerControl", "Konnte Datei nicht löschen: ${file.name}")
                    }
                }
            }

            if (!directory.delete()) {
                Log.e("ImagePickerControl", "Konnte Ordner nicht löschen: ${directory.name}")
            }
        }
    }


}

