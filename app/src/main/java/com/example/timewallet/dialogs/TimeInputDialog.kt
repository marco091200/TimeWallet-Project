package com.example.timewallet.dialogs

import androidx.fragment.app.FragmentManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar

/**
 * Diese Klasse ermöglicht das Öffnen eines Zeit-Pickers zur Auswahl einer Uhrzeit
 * und das Setzen der ausgewählten Uhrzeit in ein TextInputEditText-Feld.
 *
 * @author Marco Martins
 * @created 04.11.2023
 */
class TimeInputDialog {

    /**
     * Öffnet den Zeit-Picker und setzt die ausgewählte Uhrzeit in das übergebene TextInputEditText.
     *
     * @param timeInput Das TextInputEditText, in das die ausgewählte Uhrzeit gesetzt wird.
     * @param parentFragmentManager Der FragmentManager, der verwendet wird, um den Dialog anzuzeigen.
     * @param titleText Der Titel, der im Zeit-Picker angezeigt wird.
     */
    fun timeInputOpener(timeInput : TextInputEditText, parentFragmentManager: FragmentManager, titleText : String) {
        timeInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val startTimePicker = MaterialTimePicker
                .Builder()
                .setInputMode(INPUT_MODE_CLOCK)
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText(titleText)
                .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(calendar.get(Calendar.MINUTE))
                .build()
            startTimePicker.show(parentFragmentManager, "timePicker")
            startTimePicker.addOnPositiveButtonClickListener {
                val selectedHour = startTimePicker.hour
                val selectedMinute = startTimePicker.minute
                val selectedTimeStart = String.format("%02d:%02d", selectedHour, selectedMinute)
                timeInput.setText(selectedTimeStart)
            }
        }
    }
}