package com.example.timewallet.dialogs

import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateInputDialog {


fun dateInputDialogOpener(dateInput : TextInputEditText, parentFragmentManager : FragmentManager){
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
}