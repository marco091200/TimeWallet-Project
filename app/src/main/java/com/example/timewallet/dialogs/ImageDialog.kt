package com.example.timewallet.dialogs


import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ImageDialog {

    fun showImageDialog(context: Context, callback: (action: ImageAction) -> Unit) {
        val builder = MaterialAlertDialogBuilder(context)
        builder.setTitle("Bild bearbeiten")
        builder.setMessage("Wählen Sie eine der folgenden Möglichkeiten aus:")

        // Hinzufügen des Buttons zum Hinzufügen eines Bildes
        builder.setPositiveButton("Aktuelles Bild ändern") { _, _ ->
            callback(ImageAction.ADD)
        }

        // Hinzufügen des Buttons zum Löschen des aktuellen Bildes
        builder.setNegativeButton("Aktuelles Bild löschen") { _, _ ->
            callback(ImageAction.DELETE)
        }

        // Hinzufügen des Buttons zum Abbrechen
        builder.setNeutralButton("Abbrechen") { _, _ ->
            callback(ImageAction.OK)
        }

        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()
    }

    enum class ImageAction {
        ADD, DELETE, OK
    }
}
