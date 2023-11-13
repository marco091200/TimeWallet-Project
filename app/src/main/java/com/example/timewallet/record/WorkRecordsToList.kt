package com.example.timewallet.record

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class WorkRecordsToList {
    fun readWorkRecordsFromFile(context: Context, fileName: String): List<WorkRecord> {
        val workRecords = mutableListOf<WorkRecord>()
        try {
            val fileInputStream = context.openFileInput(fileName)
            val reader = BufferedReader(InputStreamReader(fileInputStream))
            var line: String? = reader.readLine()
            while (line != null) {
                val parts = line.split(",")
                if (parts.size == 5) {
                    val workRecord =
                        WorkRecord(parts[0], parts[1], parts[2], parts[3], parts[4])
                    workRecords.add(workRecord)
                }
                line = reader.readLine()
            }
            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return workRecords
    }
}
