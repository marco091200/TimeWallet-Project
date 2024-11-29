package com.example.timewallet.dialogs

import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar

/**
 * Diese Klasse stellt eine Methode zur Verfügung, um einen Zeitwahl-Dialog zu öffnen,
 * bei dem der Benutzer eine Uhrzeit auswählen kann. Die gewählte Uhrzeit wird dann
 * an eine Callback-Methode übergeben.
 *
 * @author Marco Martins
 * @created 27.12.2024
 */
class ReminderDialog {

    /**
     * Öffnet einen Material Time Picker, um eine Uhrzeit auszuwählen, und ruft eine Callback-Methode
     * auf, wenn der Benutzer eine Uhrzeit auswählt.
     *
     * @param parentFragmentManager Der FragmentManager, um den Dialog anzuzeigen.
     * @param titleText Der Titel des Dialogs, der angezeigt wird.
     * @param onTimeSelected Ein Lambda, das die ausgewählte Uhrzeit als String erhält.
     */
    fun timeInputOpener(
        parentFragmentManager: FragmentManager,
        titleText: String,
        onTimeSelected: (String) -> Unit
    ) {
        val calendar = Calendar.getInstance()
        val startTimePicker = MaterialTimePicker
            .Builder()
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setTitleText(titleText)
            .setHour(calendar.get(Calendar.HOUR_OF_DAY))
            .setMinute(calendar.get(Calendar.MINUTE))
            .build()

        startTimePicker.show(parentFragmentManager, "timePicker")

        startTimePicker.addOnPositiveButtonClickListener {
            val selectedHour = startTimePicker.hour
            val selectedMinute = startTimePicker.minute
            val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            onTimeSelected(selectedTime)
        }
    }
}