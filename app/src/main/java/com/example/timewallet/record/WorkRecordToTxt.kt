package com.example.timewallet.record

import android.content.Context
import com.example.timewallet.dialogs.ReplaceWorkRecordDialog
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class WorkRecordToTxt {
    companion object {
        fun saveWorkRecordToFile(context: Context, workRecord: WorkRecord, fileName : String) {
            val fileContent = "${workRecord.date},${workRecord.startTime},${workRecord.endTime},${workRecord.workedHours},${workRecord.chipInput}\n"

            try {
                val file = File(context.filesDir, fileName)

                val existingRecords = WorkRecordsToList().readWorkRecordsFromFile(context, fileName)
                if (existingRecords.any { it.date == workRecord.date }) {
                    ReplaceWorkRecordDialog(context, fileContent, fileName).showDialog()
                } else {
                    writeRecordToFile(context, fileContent, fileName)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        private fun writeRecordToFile(context: Context, fileContent: String, fileName: String) {
            try {
                context.openFileOutput(fileName, Context.MODE_APPEND).use {
                    it.write(fileContent.toByteArray())
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun writeRecordsToFile(context: Context, records: List<WorkRecord>, fileName: String) {
            try {
                context.openFileOutput(fileName, Context.MODE_PRIVATE).use { outputStream ->
                    records.forEach { record ->
                        val fileContent = "${record.date},${record.startTime},${record.endTime},${record.workedHours},${record.chipInput}\n"
                        outputStream.write(fileContent.toByteArray())
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}