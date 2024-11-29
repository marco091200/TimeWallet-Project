package com.example.timewallet.controls

import com.example.timewallet.record.WorkRecord
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs

/**
 * Diese Klasse verwaltet die Logik zur Berechnung von Arbeitszeiten, dem Umgang mit Arbeitsdatensätzen
 * und weiteren zeiterfassungsbezogenen Funktionen.
 *
 * @author Marco Martins
 * @created 27.11.2023
 */
class WorkRecordControl {

    /**
     * Berechnet die Arbeitsstunden und -minuten zwischen zwei gegebenen Zeiten (Start- und Endzeit).
     *
     * @param startTime Die Startzeit als `TextInputEditText`.
     * @param endTime Die Endzeit als `TextInputEditText`.
     * @return Ein formatiertes String im Format "HH:mm", das die berechneten Arbeitsstunden und Minuten darstellt.
     */
    fun workedHoursCalculator(startTime: TextInputEditText, endTime: TextInputEditText): String {
        val startTimeStr = startTime.text.toString()
        val endTimeStr = endTime.text.toString()

        val startDateTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(startTimeStr)
        val endDateTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(endTimeStr)

        var differenceMillis = endDateTime!!.time - startDateTime!!.time

        if (differenceMillis < 0) {
            differenceMillis += 24 * 60 * 60 * 1000
        }

        val hours = differenceMillis / (1000 * 60 * 60)
        val minutes = (differenceMillis % (1000 * 60 * 60)) / (1000 * 60)
        return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes)
    }

    /**
     * Gibt den Text des ausgewählten Chips in einer `ChipGroup` zurück.
     *
     * @param chipGroup Die `ChipGroup`, die die Chips enthält.
     * @return Der Text des ausgewählten Chips.
     */
    fun chipControl(chipGroup: ChipGroup): String {
        val checkedChipIds = chipGroup.checkedChipIds
        var chipText = String()
        for (chipId in checkedChipIds) {
            val chip = chipGroup.findViewById<Chip>(chipId)
            chipText = chip.text.toString()

        }
        return chipText
    }

    /**
     * Berechnet die insgesamt geleisteten Stunden und Minuten für einen Monat.
     *
     * @param workRecord Die Liste von `WorkRecord`-Objekten für den Monat.
     * @return Ein String, der die Gesamtstunden und Minuten darstellt.
     */
    fun hoursMonth(workRecord: List<WorkRecord>): String {

        val totalMinutes = workRecord.sumOf { parseToMinutes(it.workedHours) }


        val totalHours = totalMinutes / 60
        val remainingMinutes = totalMinutes % 60

        return "$totalHours Std. $remainingMinutes Min."
    }

    /**
     * Berechnet die Überstunden für einen Monat im Vergleich zu den Sollstunden.
     *
     * @param workRecord Die Liste von `WorkRecord`-Objekten für den Monat.
     * @param hoursPerMonth Die Sollstunden pro Monat im Format "HH:mm".
     * @return Ein String, der die Überstunden darstellt.
     */
    fun overtime(workRecord: List<WorkRecord>, hoursPerMonth: String?): String {

        val totalMinutes = workRecord.sumOf { parseToMinutes(it.workedHours) }

        val standardHoursPerMonth = if (hoursPerMonth != null) {
            parseToMinutes("$hoursPerMonth:00")
        } else {
            parseToMinutes("0:00")
        }

        val overtimeMinutes = totalMinutes - standardHoursPerMonth
        val overtimeHours = overtimeMinutes / 60
        var remainingMinutes = overtimeMinutes % 60
        if (remainingMinutes < 0) {
            remainingMinutes = abs(remainingMinutes)
        }
        val sign = if (overtimeMinutes > 0) "+" else ""

        return "$sign$overtimeHours Std. $remainingMinutes Min."
    }

    /**
     * Bestimmt den Monat, in dem die Berechnung der Überstunden beginnen soll.
     *
     * @param workRecordsForYear Die Liste der Arbeitsdatensätze für das Jahr.
     * @param hoursPerMonth Die Sollstunden pro Monat im Format "HH:mm".
     * @return Der Monat (als Ganzzahl), ab dem Überstunden berechnet werden sollen.
     */
    private fun startMonth(
        workRecordsForYear: MutableList<List<WorkRecord>>,
        hoursPerMonth: String?
    ): Int {
        var startMonth = 0

        for (i in 0..LocalDate.now().monthValue) {
            val workRecords = workRecordsForYear[i]
            val checkFirstMonth = overtime(workRecords, hoursPerMonth)
            val regex = Regex("\\d+")
            val matches = regex.findAll(checkFirstMonth)
            val numbers = matches.map { it.value.toInt() }.toList()

            val hours = numbers[0]
            if (hours.toString() != hoursPerMonth) {
                startMonth = i
                break
            } else {
                startMonth = LocalDate.now().monthValue - 1
            }
        }
        return startMonth
    }

    /**
     * Berechnet die Überstunden für das gesamte Jahr basierend auf den Arbeitsdatensätzen.
     *
     * @param workRecordsForYear Die Liste von Arbeitsdatensätzen für das Jahr.
     * @param hoursPerMonth Die Sollstunden pro Monat im Format "HH:mm".
     * @return Ein String, der die Gesamtüberstunden für das Jahr darstellt.
     */
    @Suppress("KotlinConstantConditions")
    fun overtimeYear(
        workRecordsForYear: MutableList<List<WorkRecord>>,
        hoursPerMonth: String?
    ): String {
        var totalOvertimeHours = 0
        var totalOvertimeMinutes = 0

        val startMonth = startMonth(workRecordsForYear, hoursPerMonth)

        for (i in startMonth..<LocalDate.now().monthValue) {
            if (i < workRecordsForYear.size) {
                val workRecords = workRecordsForYear[i]
                val overtimeResult = overtime(workRecords, hoursPerMonth)

                val regex = Regex("\\d+")
                val matches = regex.findAll(overtimeResult)
                val numbers = matches.map { it.value.toInt() }.toList()

                if (numbers.size >= 2) {
                    val sign = if (overtimeResult.contains("+")) 1 else -1
                    val hours = numbers[0] * sign
                    val minutes = numbers[1] * sign

                    totalOvertimeHours += hours
                    totalOvertimeMinutes += minutes

                    if (totalOvertimeMinutes >= 60) {
                        totalOvertimeHours += totalOvertimeMinutes / 60
                        totalOvertimeMinutes %= 60
                    } else if (totalOvertimeMinutes < 0) {
                        totalOvertimeHours -= abs(totalOvertimeMinutes) / 60
                        totalOvertimeMinutes = abs(totalOvertimeMinutes) % 60
                    }
                }
            }
        }

        val sign = if ((totalOvertimeHours > 0 && totalOvertimeMinutes >= 0) ||
            (totalOvertimeHours > 0 && totalOvertimeMinutes >= 0)
        ) "+" else ""

        return "$sign$totalOvertimeHours Std. $totalOvertimeMinutes Min."
    }

    /**
     * Wandelt Arbeitsstunden im Format "HH:mm" in Minuten um.
     *
     * @param workedHours Die Arbeitsstunden im Format "HH:mm".
     * @return Die umgerechneten Minuten.
     */
    private fun parseToMinutes(workedHours: String): Int {
        if (workedHours.isEmpty()) {
            return 0
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

        return 0
    }

    /**
     * Erzeugt eine Liste von Monaten zwischen zwei gegebenen Daten.
     *
     * @param startDate Das Startdatum.
     * @param endDate Das Enddatum.
     * @return Eine Liste von Monaten im Format "MM-yyyy".
     */
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

    /**
     * Kombiniert alle Arbeitsdatensätze eines Jahres in eine einzige Liste.
     *
     * @param workRecords Die Liste von Listen von Arbeitsdatensätzen für das Jahr.
     * @return Eine kombinierte Liste aller Arbeitsdatensätze.
     */
    fun combineWorkRecordsForYear(workRecords: List<List<WorkRecord>>): List<WorkRecord> {
        val combinedList = mutableListOf<WorkRecord>()
        for (monthRecords in workRecords) {
            combinedList.addAll(monthRecords)
        }
        return combinedList
    }

    /**
     * Zählt die Anzahl der Krankheitstage in einem Arbeitsdatensatz.
     *
     * @param workRecord Die Liste von Arbeitsdatensätzen.
     * @return Ein String, der die Anzahl der Krankheitstage darstellt.
     */
    fun sickCounter(workRecord: List<WorkRecord>): String {
        val sick = workRecord.count { it.chipInput.contains("Krank", ignoreCase = true) }
        if (sick == 1) {
            return "$sick Tag"
        }
        return "$sick Tage"
    }

    /**
     * Zählt die Anzahl der freien Tage (Resttage) in einem Arbeitsdatensatz.
     *
     * @param workRecord Die Liste von Arbeitsdatensätzen.
     * @return Ein String, der die Anzahl der freien Tage darstellt.
     */
    fun restDayCounter(workRecord: List<WorkRecord>): String {
        val restDay = workRecord.count { it.chipInput.contains("Frei", ignoreCase = true) }
        if (restDay == 1) {
            return "$restDay Tag"
        }
        return "$restDay Tage"
    }

    /**
     * Zählt die Anzahl der Urlaubstage in einem Arbeitsdatensatz.
     *
     * @param workRecord Die Liste von Arbeitsdatensätzen.
     * @return Ein String, der die Anzahl der Urlaubstage darstellt.
     */
    fun vacationCounter(workRecord: List<WorkRecord>): String {
        val vacation = workRecord.count { it.chipInput.contains("Urlaub", ignoreCase = true) }
        if (vacation == 1) {
            return "$vacation Tag"
        }
        return "$vacation Tage"
    }

    /**
     * Gibt eine Übersicht der Arbeitszeitdaten für einen bestimmten Tag zurück.
     *
     * @param workRecord Die Liste von Arbeitsdatensätzen.
     * @param date Das Datum, für das die Übersicht erstellt wird.
     * @return Eine Liste von Strings mit den Arbeitszeitinformationen.
     */
    fun calenderDayView(workRecord: List<WorkRecord>, date: String): List<String> {
        val currentDateData: MutableList<String> = mutableListOf()


        if (workRecord.isEmpty()) {
            repeat(4) { currentDateData.add("-") }
            return currentDateData
        }


        val selectedWorkRecord = workRecord.find { it.date == date }

        if (selectedWorkRecord != null) {
            currentDateData.add(if (selectedWorkRecord.startTime.isBlank()) "-" else selectedWorkRecord.startTime + " Uhr")
            currentDateData.add(if (selectedWorkRecord.endTime.isBlank()) "-" else selectedWorkRecord.endTime + " Uhr")
            currentDateData.add(if (selectedWorkRecord.workedHours.isBlank()) "-" else selectedWorkRecord.workedHours + " Std.")
            currentDateData.add(selectedWorkRecord.chipInput.ifBlank { "-" })
        } else {
            repeat(4) { currentDateData.add("-") }
        }

        return currentDateData
    }

}