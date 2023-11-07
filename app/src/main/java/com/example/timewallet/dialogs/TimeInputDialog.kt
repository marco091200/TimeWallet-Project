package com.example.timewallet.dialogs

import androidx.fragment.app.FragmentManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar

class TimeInputDialog {

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