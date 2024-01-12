package com.example.timewallet.dialogs

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ReplaceWorkRecordDialog(
    private val context: Context,
    private val fileContent: String,
    private val fileName: String
) {
        fun showDialog() {
            val alertDialog = MaterialAlertDialogBuilder(context)
            alertDialog.setTitle("Eintrag existiert bereits")
            alertDialog.setMessage("Möchten Sie den vorhandenen Eintrag ersetzen?")
            alertDialog.setNegativeButton("Ja") { _: DialogInterface, _: Int ->
                context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
                    it.write(fileContent.toByteArray())
                }
            }
            alertDialog.setPositiveButton("Nein") { _: DialogInterface, _: Int -> }
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
}