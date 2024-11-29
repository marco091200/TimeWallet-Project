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

/**
 * Die `ImagePickerControl`-Klasse ermöglicht das Auswählen, Speichern und Löschen von Bildern in der App.
 * Sie verwaltet den Zugriff auf die Galerie, speichert ausgewählte Bilder und bietet Funktionen
 * zum Verwalten des Bildspeicherordners.
 *
 * @author Marco Martins
 * @created 15.12.2023
 */
@Suppress("DEPRECATION")
class ImagePickerControl(private val fragment: Fragment) {
    private val pickImageRequest = 1

    /**
     * Öffnet die Galerie, um ein Bild auszuwählen.
     */
    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        fragment.startActivityForResult(intent, pickImageRequest)
    }

    /**
     * Speichert das Bild im internen Speicher, verkleinert es falls nötig.
     * @param bitmap Das zu speichernde Bild.
     * @return Die gespeicherte Datei oder null bei Fehlern.
     */
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

    /**
     * Gibt die gespeicherte Bilddatei zurück oder null, wenn sie nicht existiert.
     * @return Die Bilddatei oder null.
     */
    fun getImageFile(): File? {
        val directory = File(fragment.requireContext().filesDir, "images")
        return if (directory.exists()) {
            File(directory, "current_image.jpg")
        } else {
            null
        }
    }

    /**
     * Behandelt das Ergebnis der Bildauswahl aus der Galerie und zeigt es im ImageView an.
     * @param requestCode Der RequestCode der Aktivität.
     * @param resultCode Der ResultCode der Aktivität.
     * @param data Die Absicht mit den Bilddaten.
     * @param imageView Das ImageView, in dem das Bild angezeigt wird.
     */
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


    /**
     * Löscht den Ordner und alle darin enthaltenen Bilder.
     */
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

