package com.example.timewallet.dialogs


import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Diese Klasse stellt einen Dialog zur Verfügung, der es dem Benutzer ermöglicht,
 * ein Bild zu bearbeiten. Der Benutzer kann entscheiden, ob er ein Bild hinzufügen,
 * das aktuelle Bild löschen oder den Vorgang abbrechen möchte.
 *
 * @author Marco Martins
 * @created 21.12.2023
 */
class ImageDialog {

    /**
     * Zeigt einen Dialog an, in dem der Benutzer eine Aktion zum Bearbeiten eines Bildes auswählen kann.
     * Der Dialog bietet die Optionen, das aktuelle Bild zu ändern, das Bild zu löschen oder abzubrechen.
     *
     * @param context Der Kontext, in dem der Dialog angezeigt wird.
     * @param callback Eine Callback-Funktion, die die ausgewählte Aktion übermittelt.
     * Es werden Werte vom Typ [ImageAction] übergeben, die die gewünschte Aktion widerspiegeln.
     */
    fun showImageDialog(context: Context, callback: (action: ImageAction) -> Unit) {
        val builder = MaterialAlertDialogBuilder(context)
        builder.setTitle("Bild bearbeiten")
        builder.setMessage("Wählen Sie eine der folgenden Möglichkeiten aus:")

        builder.setPositiveButton("Aktuelles Bild ändern") { _, _ ->
            callback(ImageAction.ADD)
        }

        builder.setNegativeButton("Aktuelles Bild löschen") { _, _ ->
            callback(ImageAction.DELETE)
        }

        builder.setNeutralButton("Abbrechen") { _, _ ->
            callback(ImageAction.OK)
        }

        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()
    }

    /**
     * Enum, das die möglichen Aktionen darstellt, die der Benutzer im Bildbearbeitungsdialog auswählen kann.
     * - [ADD]: Das aktuelle Bild wird geändert.
     * - [DELETE]: Das aktuelle Bild wird gelöscht.
     * - [OK]: Der Benutzer bricht den Vorgang ab.
     */
    enum class ImageAction {
        ADD, DELETE, OK
    }
}
