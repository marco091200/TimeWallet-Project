package com.example.timewallet.dialogs

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ExistingTxtFile {

    companion object {
        fun displayOverwriteQuestion(context: Context, callback: (accepted: Boolean) -> Unit) {
            val builder = MaterialAlertDialogBuilder(context)
            builder.setTitle("Überschreiben")
            builder.setMessage("Es existiert bereits ein Datensatz mit diesem Datum!\nMöchten Sie diesen Datensatz überschreiben?")
            builder.setPositiveButton("Überschreiben") { dialog, which ->
                callback(true)
            }
            builder.setCancelable(false)
            val dialog = builder.create()
            dialog.show()
        }
    }
}