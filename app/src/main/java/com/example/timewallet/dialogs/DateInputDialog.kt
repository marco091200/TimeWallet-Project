package com.example.timewallet.dialogs

import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Diese Klasse stellt Dialoge zur Auswahl von Datum oder Datumsbereichen zur Verfügung.
 * Der Benutzer kann ein einzelnes Datum oder einen Zeitraum auswählen.
 *
 *
 * @author Marco Martins
 * @created 04.11.2023
 */
class DateInputDialog {

    /**
     * Öffnet einen Dialog zur Auswahl eines einzelnen Datums.
     * Wenn der Benutzer ein Datum auswählt, wird das Datum im Format "dd.MM.yyyy" in das übergebene Textfeld eingetragen.
     *
     * @param dateInput Das Textfeld, in das das ausgewählte Datum eingefügt wird.
     * @param parentFragmentManager Der FragmentManager, der verwendet wird, um den Dialog anzuzeigen.
     */
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

    /**
     * Öffnet einen Dialog zur Auswahl eines Datumsbereichs (Start- und Enddatum).
     * Wenn der Benutzer einen Bereich auswählt, wird dieser in das gewünschte Format konvertiert und an die übergebene Callback-Funktion übergeben.
     *
     * @param parentFragmentManager Der FragmentManager, der verwendet wird, um den Dialog anzuzeigen.
     * @param onDateSelected Callback-Funktion, die die ausgewählten Start- und Enddaten als Strings empfängt.
     */
    fun dateButtonDialogOpener(
        parentFragmentManager: FragmentManager,
        onDateSelected: (startDate: String, endDate: String) -> Unit
    ) {
        val datePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setPositiveButtonText("Download")
            .setTitleText("Wählen Sie eine Zeitspanne für den Download:")
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