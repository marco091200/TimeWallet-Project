package com.example.timewallet.dialogs

import android.content.Context
import android.content.DialogInterface
import com.example.timewallet.record.WorkRecord
import com.example.timewallet.record.WorkRecordToTxt
import com.example.timewallet.record.WorkRecordsToList
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.IOException

/**
 * Diese Klasse zeigt einen Dialog an, wenn versucht wird, einen neuen WorkRecord zu speichern,
 * der bereits ein bestehendes Datum überschreibt. Sie fragt den Benutzer, ob er den bestehenden
 * Datensatz ersetzen möchte.
 *
 * @param context Der Kontext, in dem der Dialog angezeigt wird.
 *
 * @author Marco Martins
 * @created 12.01.2024
 */
class ReplaceWorkRecordDialog(
    private val context: Context
) {

    /**
     * Schnittstelle, die Methoden enthält, um den Benutzer-Entscheid über das Ersetzen eines WorkRecords zu erhalten.
     */
    interface OnReplaceListener {
        fun onReplaceConfirmed()
        fun onReplaceCanceled()
    }

    /**
     * Zeigt den Dialog an, der den Benutzer fragt, ob er einen bestehenden WorkRecord ersetzen möchte.
     *
     * @param listener Ein Listener, der auf die Bestätigung oder Stornierung der Benutzerentscheidung reagiert.
     */
    fun showDialog(listener: OnReplaceListener) {
        val alertDialog = MaterialAlertDialogBuilder(context)
        alertDialog.setTitle("Eintrag existiert bereits")
        alertDialog.setMessage("Es existiert bereits ein Datensatz mit diesem Datum!\nMöchten Sie diesen Datensatz überschreiben?")
        alertDialog.setNegativeButton("Ja") { _: DialogInterface, _: Int ->
            listener.onReplaceConfirmed()
        }
        alertDialog.setPositiveButton("Nein") { _: DialogInterface, _: Int ->
            listener.onReplaceCanceled()
        }
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}