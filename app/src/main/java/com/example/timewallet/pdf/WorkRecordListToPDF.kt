package com.example.timewallet.pdf

import android.os.Environment
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
    fun createPDF (workRecord: List<WorkRecord>) : File {
        // Erstelle ein Dateiobjekt für den Download-Ordner
        val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val sortedWorkRecords = workRecord.sortedBy { it.date }

        // Erstelle eine PDF-Datei im Download-Ordner
        val timeStamp = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
        val pdfFileName = "WorkRecords_$timeStamp.pdf"
        val pdfFile = File(downloadDir, pdfFileName)

        // Erstelle ein PdfWriter
        val writer = PdfWriter(FileOutputStream(pdfFile))

        // Erstelle ein PdfDocument
        val pdf = PdfDocument(writer)

        // Erstelle ein Document
        val document = Document(pdf)

        // Erstelle eine Tabelle mit 5 Spalten (Datum, Startzeit, Endzeit, Arbeitsstunden, Chip-Input)
        val table = Table(floatArrayOf(100f, 100f, 100f, 100f, 100f))

        // Füge Header-Zellen hinzu
        table.addHeaderCell("Datum")
        table.addHeaderCell("Startzeit")
        table.addHeaderCell("Endzeit")
        table.addHeaderCell("Arbeitsstunden")
        table.addHeaderCell("Sonderfall")

        // Füge Daten-Zellen hinzu
        for (workRecord in sortedWorkRecords) {
            table.addCell(workRecord.date)
            table.addCell(workRecord.startTime)
            table.addCell(workRecord.endTime)
            table.addCell(workRecord.workedHours.toString())
            table.addCell(workRecord.chipInput)
        }

        // Füge die Tabelle zum Dokument hinzu
        document.add(table)

        // Schließe das Dokument
        document.close()

        // Gib die erstellte PDF-Datei zurück
        return pdfFile
    }
}