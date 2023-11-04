package com.example.timewallet.dialogs

import androidx.fragment.app.FragmentManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker

class TimeInputDialog {

    fun timeInputOpener(timeInput : TextInputEditText, parentFragmentManager: FragmentManager) {
        timeInput.setOnClickListener {
            val startTimePicker = MaterialTimePicker
                .Builder()
                .setTitleText("Wählen Sie eine Zeit:")
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