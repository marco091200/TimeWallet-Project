package com.example.timewallet.dialogs

import androidx.fragment.app.FragmentManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.TimeFormat

class TimeInputDialog {

    fun timeInputOpener(timeInput : TextInputEditText, parentFragmentManager: FragmentManager, titleText : String) {
        timeInput.setOnClickListener {
            val startTimePicker = MaterialTimePicker
                .Builder()
                .setInputMode(INPUT_MODE_CLOCK)
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText(titleText)
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