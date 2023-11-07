package com.example.timewallet.record

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class WorkRecordToTxt {
    companion object {
        fun saveWorkRecordToFile(context: Context, workRecord: WorkRecord, fileName : String) {
            val fileContent = "${workRecord.date},${workRecord.startTime},${workRecord.endTime},${workRecord.workedHours},${workRecord.chipInput}\n"

            try {
                val file = File(context.filesDir, fileName)
                val fileOutputStream: FileOutputStream

                if (file.exists()) {
                    val existingRecords = WorkRecordsToList().readWorkRecordsFromFile(context, fileName)
                    if (existingRecords.none { it.date == workRecord.date }) {
                        // Kein Eintrag mit dem gleichen Datum gefunden, füge den neuen Eintrag hinzu
                        fileOutputStream = context.openFileOutput(fileName, Context.MODE_APPEND)
                    } else {
                        // Eintrag mit dem gleichen Datum gefunden, nicht hinzufügen
                        return
                    }
                } else {
                    // Datei existiert nicht, erstelle sie und füge den Eintrag hinzu
                    file.createNewFile()
                    fileOutputStream = context.openFileOutput(fileName, Context.MODE_APPEND)
                }

                fileOutputStream.write(fileContent.toByteArray())
                fileOutputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}