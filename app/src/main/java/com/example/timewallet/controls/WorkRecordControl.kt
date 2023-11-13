package com.example.timewallet.controls

import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
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

    fun chipControl (chipGroup: ChipGroup) : String {
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
}