package com.example.timewallet.controls

import com.example.timewallet.record.WorkRecord
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class WorkRecordControl {
    fun workedHoursCalculator(startTime: TextInputEditText, endTime: TextInputEditText): String {
        val startTimeStr = startTime.text.toString()
        val endTimeStr = endTime.text.toString()

        // Parse die eingegebenen Zeiten
        val startDateTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(startTimeStr)
        val endDateTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(endTimeStr)

        // Berechne die Differenz in Millisekunden
        var differenceMillis = endDateTime.time - startDateTime.time

        // Behandle den Fall, wenn die Endzeit vor der Startzeit liegt (über Mitternacht)
        if (differenceMillis < 0) {
            // Addiere 24 Stunden, um den Zeitunterschied zu korrigieren
            differenceMillis += 24 * 60 * 60 * 1000
        }

        // Konvertiere die Differenz in Stunden und Minuten
        val hours = differenceMillis / (1000 * 60 * 60)
        val minutes = (differenceMillis % (1000 * 60 * 60)) / (1000 * 60)
        // Formatiere die Arbeitsstunden als String
        return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes)
    }

    fun chipControl(chipGroup: ChipGroup): String {
        val checkedChipIds = chipGroup.checkedChipIds
        var chipText = String()
        for (chipId in checkedChipIds) {
            val chip = chipGroup.findViewById<Chip>(chipId)
            // Hier kannst du mit dem ausgewählten Chip arbeiten
            chipText = chip.text.toString()
            // Füge den Code hinzu, um mit dem ausgewählten Chip zu arbeiten
        }
        return chipText
    }

    fun hoursMonth(workRecord: List<WorkRecord>): String {
        // Summiere die gearbeiteten Stunden und Minuten im aktuellen Monat
        val totalMinutes = workRecord.sumBy { parseToMinutes(it.workedHours) }

        // Berechne Stunden und Minuten
        val totalHours = totalMinutes / 60
        val remainingMinutes = totalMinutes % 60

        return "$totalHours Std. $remainingMinutes Min."
    }

    fun overtime(workRecord: List<WorkRecord>, hoursPerMonth :String?) : String {
        // Summiere die gearbeiteten Stunden und Minuten
        val totalMinutes = workRecord.sumBy { parseToMinutes(it.workedHours) }

        // Berechne die Soll-Arbeitszeit im Monat (falls angegeben)
        val standardHoursPerMonth = if (hoursPerMonth != null) {
            parseToMinutes("$hoursPerMonth:00")
        } else {
            parseToMinutes("0:00")
        }

        // Berechne Überstunden
        val overtimeMinutes = totalMinutes - standardHoursPerMonth
        val overtimeHours = overtimeMinutes / 60
        val remainingMinutes = overtimeMinutes % 60

        // Überprüfe, ob Überstunden positiv oder negativ sind
        val sign = if (overtimeMinutes > 0) "+" else ""

        return "$sign$overtimeHours Std. $remainingMinutes Min."
    }

    fun parseToMinutes(workedHours: String): Int {
        if (workedHours.isEmpty()) {
            return 0  // Rückgabewert für leere Zeichenkette
        }

        val parts = workedHours.split(":")

        if (parts.size == 2) {
            try {
                val hoursValue = parts[0].toInt()
                val minutesValue = parts[1].toInt()
                return hoursValue * 60 + minutesValue
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }

        return 0  // Rückgabewert für ungültiges Format
    }

    fun generateMonthsBetweenDates(startDate: Date, endDate: Date): List<String> {
        val calendar = Calendar.getInstance()
        calendar.time = startDate
        val endCalendar = Calendar.getInstance()
        endCalendar.time = endDate

        val months = mutableListOf<String>()

        while (calendar.before(endCalendar) || calendar == endCalendar) {
            val formattedMonth =
                SimpleDateFormat("MM-yyyy", Locale.getDefault()).format(calendar.time)
            months.add(formattedMonth)
            calendar.add(Calendar.MONTH, 1)
        }

        return months
    }

    fun sickCounter(workRecord: List<WorkRecord>): String {
        val sick = workRecord.count { it.chipInput.contains("Krank", ignoreCase = true) }
        if (sick == 1) {
            return "$sick Tag"
        }
        return "$sick Tage"
    }

    fun calenderDayView(workRecord: List<WorkRecord>, date: String): List<String> {
        val currentDateData: MutableList<String> = mutableListOf()

        // Überprüfe, ob die Liste workRecord leer ist
        if (workRecord.isEmpty()) {
            repeat(4) { currentDateData.add("-") }
            return currentDateData
        }

        // Suche nach dem passenden WorkRecord für das angegebene Datum
        val selectedWorkRecord = workRecord.find { it.date == date }

        // Überprüfe, ob ein passender WorkRecord gefunden wurde
        if (selectedWorkRecord != null) {
            // Füge die relevanten Informationen für den Tag zur Liste hinzu
            currentDateData.add(if (selectedWorkRecord.startTime.isBlank()) "-" else selectedWorkRecord.startTime + " Uhr")
            currentDateData.add(if (selectedWorkRecord.endTime.isBlank()) "-" else selectedWorkRecord.endTime + " Uhr")
            currentDateData.add(if (selectedWorkRecord.workedHours.isBlank()) "-" else selectedWorkRecord.workedHours + " Std.")
            currentDateData.add(if (selectedWorkRecord.chipInput.isBlank()) "-" else selectedWorkRecord.chipInput)
        } else {
            // Wenn kein passender WorkRecord für das Datum gefunden wurde
            repeat(4) { currentDateData.add("-") }
        }

        return currentDateData
    }

}