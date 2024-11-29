package com.example.timewallet.record

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
/**
 * Diese Klasse liest Arbeitsaufzeichnungen aus einer Datei und gibt sie als Liste von [WorkRecord] zurück.
 *
 * @author Marco Martins
 * @created 07.11.2023
 */
class WorkRecordsToList {

    /**
     * Liest die Arbeitsaufzeichnungen aus einer Datei und gibt sie als Liste von [WorkRecord] zurück.
     *
     * Jede Zeile der Datei wird in eine [WorkRecord]-Instanz umgewandelt.
     *
     * @param context Der Kontext der Anwendung, um auf die Datei im internen Speicher zuzugreifen.
     * @param fileName Der Name der Datei, aus der die Arbeitsaufzeichnungen gelesen werden.
     * @return Eine Liste von [WorkRecord]-Objekten, die aus der Datei gelesen wurden.
     */
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
