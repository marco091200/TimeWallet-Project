package com.example.timewallet.record

import android.content.Context
import com.example.timewallet.dialogs.ReplaceWorkRecordDialog
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

class WorkRecordToTxt {
    companion object {
        fun saveWorkRecordToFile(context: Context, workRecord: WorkRecord, fileName: String) {
            val fileContent =
                "${workRecord.date},${workRecord.startTime},${workRecord.endTime},${workRecord.workedHours},${workRecord.chipInput}\n"

            try {
                File(context.filesDir, fileName)

                val existingRecords = WorkRecordsToList().readWorkRecordsFromFile(context, fileName)
                if (existingRecords.any { it.date == workRecord.date }) {
                    ReplaceWorkRecordDialog(context).showDialog(object :
                        ReplaceWorkRecordDialog.OnReplaceListener {
                        override fun onReplaceConfirmed() {
                            deleteRecordByDate(context, fileName, workRecord.date)
                            writeRecordToFile(context,fileContent,fileName)
                        }

                        override fun onReplaceCanceled() {
                        }
                    })
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
        fun deleteRecordByDate(context: Context, fileName: String, target: String) {
        val file = File(context.filesDir, fileName)

        try {
            val reader = BufferedReader(FileReader(file))
            val writer = BufferedWriter(FileWriter(File(context.filesDir, "temp_$fileName")))

            var line: String? = reader.readLine()

            while (line != null) {
                // Check if the line contains the target to delete
                if (!line.contains(target)) {
                    writer.write(line)
                    writer.newLine()
                }
                line = reader.readLine()
            }

            writer.close()
            reader.close()

            // Delete the original file
            file.delete()

            // Rename the temporary file to the original file name
            val tempFile = File(context.filesDir, "temp_$fileName")
            tempFile.renameTo(file)

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

}