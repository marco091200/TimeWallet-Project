package com.example.timewallet.pdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.timewallet.R
import com.example.timewallet.controls.UserControl
import com.example.timewallet.controls.WorkRecordControl
import com.example.timewallet.record.WorkRecord
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Diese Klasse erstellt ein PDF-Dokument, das eine Liste von Arbeitsaufzeichnungen in einem bestimmten Zeitraum enthält.
 *
 * @author Marco Martins
 * @created 09.11.2023
 */
class WorkRecordListToPDF {
    private val workRecordControl = WorkRecordControl()

    /**
     * Erstellt ein PDF-Dokument für einen gegebenen Zeitraum (von Start- bis Enddatum) mit den Arbeitsaufzeichnungen.
     *
     * @param context Der Kontext der Anwendung.
     * @param workRecords Die Liste der Arbeitsaufzeichnungen.
     * @param startDate Das Startdatum des Zeitraums.
     * @param endDate Das Enddatum des Zeitraums.
     * @return Der Name der erstellten PDF-Datei.
     */
    fun createPDFPeriod(
        context: Context,
        workRecords: List<WorkRecord>,
        startDate: String,
        endDate: String
    ): String {
        try {
            val downloadDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

            val uniqueRecords = removeDuplicateRecords(workRecords)
            val filteredRecords = uniqueRecords.filter { it.date in startDate..endDate }
            val sortedWorkRecords = filteredRecords.sortedBy {
                SimpleDateFormat(
                    "dd.MM.yyyy",
                    Locale.getDefault()
                ).parse(it.date)
            }

            val userControl = UserControl(context)
            val user = userControl.readUserFromTxt()

            val pdfFileName = "Arbeitszeit-$startDate-$endDate.pdf"
            val pdfFile = File(downloadDir, pdfFileName)

            val writer = PdfWriter(FileOutputStream(pdfFile))

            val pdf = PdfDocument(writer)

            val document = Document(pdf)

            if (user?.benutzerName != "") {
                document.add(Paragraph("Benutzername: " + user?.benutzerName))
            }
            if (user?.monatlicheArbeitsstunden != "") {
                document.add(Paragraph("Monatliche Arbeitsstunden: " + user?.monatlicheArbeitsstunden))
            }

            if ((user?.benutzerName == "") && (user.monatlicheArbeitsstunden == "")) {
                document.add(Paragraph("\n"))
                document.add(Paragraph("\n"))
            }

            if ((user?.benutzerName == "") && (user.monatlicheArbeitsstunden != "")) {
                document.add(Paragraph("\n"))
            }

            if ((user?.benutzerName != "") && (user?.monatlicheArbeitsstunden == "")) {
                document.add(Paragraph("\n"))
            }

            val drawable = ContextCompat.getDrawable(context, R.drawable.timewalletbanner)
            val bitmap = (drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val bitmapData: ByteArray = stream.toByteArray()

            val imageData = ImageDataFactory.create(bitmapData)
            val image = Image(imageData)
            image.scaleToFit(140f, 100f)
            image.setFixedPosition(420f, 760f)
            document.add(image)

            val table = Table(floatArrayOf(100f, 100f, 100f, 100f, 100f))

            table.addHeaderCell("Datum").apply { setFontSize(10f) }
            table.addHeaderCell("Startzeit").apply { setFontSize(10f) }
            table.addHeaderCell("Endzeit").apply { setFontSize(10f) }
            table.addHeaderCell("Arbeitsstunden").apply { setFontSize(10f) }
            table.addHeaderCell("Bemerkung").apply { setFontSize(10f) }

            for (workRecord in sortedWorkRecords) {
                table.addCell(workRecord.date).apply { setFontSize(10f) }
                table.addCell(workRecord.startTime).apply { setFontSize(10f) }
                table.addCell(workRecord.endTime).apply { setFontSize(10f) }
                table.addCell(workRecord.workedHours).apply { setFontSize(10f) }
                table.addCell(workRecord.chipInput).apply { setFontSize(10f) }
            }

            document.add(table)
            document.add(Paragraph("\n"))
            document.add(
                Paragraph(
                    "Ihre Arbeitsstunden in diesem Zeitraum betragen insgesamt: " + workRecordControl.hoursMonth(
                        workRecords
                    )
                )
            )
            document.close()
            Toast.makeText(context, "PDF erfolgreich erstellt: $pdfFileName", Toast.LENGTH_SHORT)
                .show()
            println("PDF erfolgreich erstellt: $pdfFileName")

            return pdfFileName
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Fehler beim Erstellen der PDF!", Toast.LENGTH_SHORT).show()
            println("Fehler beim Erstellen der PDF: ${e.message}")
        }
        return ""
    }

    /**
     * Entfernt doppelte Arbeitsaufzeichnungen aus der Liste.
     *
     * @param records Die Liste der Arbeitsaufzeichnungen.
     * @return Eine Liste ohne doppelte Arbeitsaufzeichnungen.
     */
    private fun removeDuplicateRecords(records: List<WorkRecord>): List<WorkRecord> {
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