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
    private val context: Context,
    private val fileContent: String,
    private val fileName: String
) {
        fun showDialog() {
            val alertDialog = MaterialAlertDialogBuilder(context)
            alertDialog.setTitle("Eintrag existiert bereits")
            alertDialog.setMessage("Es existiert bereits ein Datensatz mit diesem Datum!\nMöchten Sie diesen Datensatz überschreiben?")
            alertDialog.setNegativeButton("Ja") { _: DialogInterface, _: Int ->
                replaceWorkRecordInFile()
            }
            alertDialog.setPositiveButton("Nein") { _: DialogInterface, _: Int -> }
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    private fun replaceWorkRecordInFile() {
        try {
            val file = File(context.filesDir, fileName)
            val existingRecords = WorkRecordsToList().readWorkRecordsFromFile(context, fileName)
            val updatedRecords = existingRecords.map {
                if (it.date == fileContent.substringBefore(",")) {
                    WorkRecord(
                        it.date,
                        it.startTime,
                        it.endTime,
                        it.workedHours,
                        it.chipInput
                    )
                } else {
                    it
                }
            }
            WorkRecordToTxt.writeRecordsToFile(context, updatedRecords, fileName)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}