package com.example.timewallet.pdf

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.example.timewallet.record.WorkRecord
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Table
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WorkRecordListToPDF {
    fun createPDFCurrentDate(context: Context, workRecords: List<WorkRecord>) {
        try {
            // Erstelle ein Dateiobjekt für den Download-Ordner
            val downloadDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val timeStamp = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
            val sortedWorkRecords = workRecords.sortedBy { it.date }

            // Erstelle eine PDF-Datei im Download-Ordner
            val pdfFileName = "Arbeitszeit-$timeStamp.pdf"
            val pdfFile = File(downloadDir, pdfFileName)

            // Rufe die Funktion createPDF auf
            val writer = PdfWriter(FileOutputStream(pdfFile))

            // Erstelle ein PdfDocument
            val pdf = PdfDocument(writer)

            // Erstelle ein Document
            val document = Document(pdf)

            // Erstelle eine Tabelle mit 5 Spalten (Datum, Startzeit, Endzeit, Arbeitsstunden, Bemerkung)
            val table = Table(floatArrayOf(100f, 100f, 100f, 100f, 100f))

            // Füge Header-Zellen hinzu
            table.addHeaderCell("Datum")
            table.addHeaderCell("Startzeit")
            table.addHeaderCell("Endzeit")
            table.addHeaderCell("Arbeitsstunden")
            table.addHeaderCell("Bemerkung")

            // Füge Daten-Zellen hinzu
            for (workRecord in sortedWorkRecords) {
                table.addCell(workRecord.date)
                table.addCell(workRecord.startTime)
                table.addCell(workRecord.endTime)
                table.addCell(workRecord.workedHours)
                table.addCell(workRecord.chipInput)
            }

            // Füge die Tabelle zum Dokument hinzu
            document.add(table)

            // Schließe das Dokument
            document.close()
            Toast.makeText(context, "PDF erfolgreich erstellt: $pdfFile", Toast.LENGTH_SHORT).show()
            // Gib eine Erfolgsmeldung aus
            println("PDF erfolgreich erstellt: $pdfFile")
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Fehler beim Erstellen der PDF!", Toast.LENGTH_SHORT).show()
            println("Fehler beim Erstellen der PDF: ${e.message}")
        }
    }

    fun createPDFPeriod(context: Context, workRecordsStart: List<WorkRecord>,workRecordsEnd: List<WorkRecord>, startDate: String, endDate:String) {
        try{
        // Erstelle ein Dateiobjekt für den Download-Ordner
        val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val sortedWorkRecordsStart = workRecordsStart.sortedBy { it.date }
        val sortedWorkRecordsEnd = workRecordsEnd.sortedBy { it.date }

        val combinedWorkRecords = sortedWorkRecordsStart + sortedWorkRecordsEnd

        val uniqueRecords = removeDuplicateRecords(combinedWorkRecords)
        // Erstelle eine PDF-Datei im Download-Ordner
        val pdfFileName = "Arbeitszeit:$startDate-$endDate.pdf"
        val pdfFile = File(downloadDir, pdfFileName)

        val writer = PdfWriter(FileOutputStream(pdfFile))

        // Erstelle ein PdfDocument
        val pdf = PdfDocument(writer)

        // Erstelle ein Document
        val document = Document(pdf)

        // Erstelle eine Tabelle mit 5 Spalten (Datum, Startzeit, Endzeit, Arbeitsstunden, Bemerkung)
        val table = Table(floatArrayOf(100f, 100f, 100f, 100f, 100f))

        // Füge Header-Zellen hinzu
        table.addHeaderCell("Datum")
        table.addHeaderCell("Startzeit")
        table.addHeaderCell("Endzeit")
        table.addHeaderCell("Arbeitsstunden")
        table.addHeaderCell("Bemerkung")

        // Füge Daten-Zellen hinzu
        for (workRecord in uniqueRecords) {
            table.addCell(workRecord.date)
            table.addCell(workRecord.startTime)
            table.addCell(workRecord.endTime)
            table.addCell(workRecord.workedHours)
            table.addCell(workRecord.chipInput)
        }

        // Füge die Tabelle zum Dokument hinzu
            document.add(table)

        // Schließe das Dokument
            document.close()
            Toast.makeText(context, "PDF erfolgreich erstellt: $pdfFile", Toast.LENGTH_SHORT).show()
            // Gib eine Erfolgsmeldung aus
            println("PDF erfolgreich erstellt: $pdfFile")
        }catch (e: Exception){
            e.printStackTrace()
            Toast.makeText(context, "Fehler beim Erstellen der PDF!", Toast.LENGTH_SHORT).show()
            println("Fehler beim Erstellen der PDF: ${e.message}")
        }
    }

    fun removeDuplicateRecords(records: List<WorkRecord>): List<WorkRecord> {
        val uniqueRecords = mutableSetOf<WorkRecord>()
        val result = mutableListOf<WorkRecord>()

        for (record in records) {
            if (uniqueRecords.add(record)) {
                result.add(record)
            }
        }

        return result
    }

}