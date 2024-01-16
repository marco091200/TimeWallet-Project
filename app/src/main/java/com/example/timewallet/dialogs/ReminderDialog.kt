package com.example.timewallet.dialogs

import androidx.fragment.app.FragmentManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar

class ReminderDialog {
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