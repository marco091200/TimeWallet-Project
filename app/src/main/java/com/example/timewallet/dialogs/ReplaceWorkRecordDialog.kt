package com.example.timewallet.dialogs

import android.content.Context
import android.content.DialogInterface
import com.example.timewallet.record.WorkRecord
import com.example.timewallet.record.WorkRecordToTxt
import com.example.timewallet.record.WorkRecordsToList
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.IOException

class ReplaceWorkRecordDialog(
    private val context: Context
) {
    interface OnReplaceListener {
        fun onReplaceConfirmed()
        fun onReplaceCanceled()
    }
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