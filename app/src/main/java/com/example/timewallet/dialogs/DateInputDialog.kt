package com.example.timewallet.dialogs

import android.widget.Button
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialDatePicker.INPUT_MODE_TEXT
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateInputDialog {


    fun dateInputDialogOpener(
        dateInput: TextInputEditText,
        parentFragmentManager: FragmentManager
    ) {
        dateInput.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Wählen Sie eine Datum aus:")
                .build()
            datePicker.show(parentFragmentManager, "datePicker")
            datePicker.addOnPositiveButtonClickListener {
                val selectedTimestamp = datePicker.selection
                if (selectedTimestamp != null) {
                    val selectedDate = Date(selectedTimestamp)
                    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedDate)
                    dateInput.setText(formattedDate)
                }
            }
        }
    }

    fun dateButtonDialogOpener(
        parentFragmentManager: FragmentManager,
        onDateSelected: (startDate: String, endDate: String) -> Unit
    ) {
        val datePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setPositiveButtonText("Download")
            .setTitleText("Wählen Sie ein Datum aus:")
            .build()

        datePicker.show(parentFragmentManager, "datePicker")

        datePicker.addOnPositiveButtonClickListener { selection ->
            if (selection != null) {
                val startCalendar = Calendar.getInstance().apply {
                    timeInMillis = selection.first
                }
                val endCalendar = Calendar.getInstance().apply {
                    timeInMillis = selection.second
                }
                val startFormattedDate =
                    SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(startCalendar.time)
                val endFormattedDate =
                    SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(endCalendar.time)
                onDateSelected(startFormattedDate, endFormattedDate)
            }
        }
    }
}