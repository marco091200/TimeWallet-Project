package com.example.timewallet.record

import android.content.Context
import com.example.timewallet.dialogs.ReplaceWorkRecordDialog
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

/**
 * Klasse zur Verwaltung und Speicherung von Arbeitsaufzeichnungen in einer Textdatei.
 *
 * Diese Klasse bietet Methoden zum Speichern, Löschen und Verarbeiten von Arbeitsdatensätzen.
 *
 * @author Marco Martins
 * @created 07.11.2023
 */
class WorkRecordToTxt {
    companion object {

        /**
         * Speichert eine Arbeitsaufzeichnung in einer Datei.
         *
         * Falls ein Datensatz mit demselben Datum bereits existiert, wird der Benutzer gefragt,
         * ob der existierende Datensatz ersetzt werden soll.
         *
         * @param context Der Kontext der Anwendung.
         * @param workRecord Die Arbeitsaufzeichnung, die gespeichert werden soll.
         * @param fileName Der Name der Datei, in der die Arbeitsaufzeichnung gespeichert wird.
         */

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
                            writeRecordToFile(context, fileContent, fileName)
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

        /**
         * Hilfsmethode zum Schreiben eines Datensatzes in eine Datei.
         *
         * @param context Der Kontext der Anwendung.
         * @param fileContent Der Inhalt, der in die Datei geschrieben werden soll.
         * @param fileName Der Name der Datei.
         */

        private fun writeRecordToFile(context: Context, fileContent: String, fileName: String) {
            try {
                context.openFileOutput(fileName, Context.MODE_APPEND).use {
                    it.write(fileContent.toByteArray())
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        /**
         * Löscht einen Datensatz anhand des Datums aus der Datei.
         *
         * Erstellt eine temporäre Datei, in der alle Datensätze außer dem Ziel-Datensatz kopiert werden.
         * Anschließend wird die ursprüngliche Datei durch die temporäre Datei ersetzt.
         *
         * @param context Der Kontext der Anwendung.
         * @param fileName Der Name der Datei, aus der der Datensatz gelöscht werden soll.
         * @param target Das Datum des Datensatzes, der gelöscht werden soll.
         */

        fun deleteRecordByDate(context: Context, fileName: String, target: String) {
            val file = File(context.filesDir, fileName)

            try {
                val reader = BufferedReader(FileReader(file))
                val writer = BufferedWriter(FileWriter(File(context.filesDir, "temp_$fileName")))

                var line: String? = reader.readLine()

                while (line != null) {
                    if (!line.contains(target)) {
                        writer.write(line)
                        writer.newLine()
                    }
                    line = reader.readLine()
                }

                writer.close()
                reader.close()
                file.delete()

                val tempFile = File(context.filesDir, "temp_$fileName")
                tempFile.renameTo(file)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}