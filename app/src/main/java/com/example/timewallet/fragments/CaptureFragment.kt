package com.example.timewallet.fragments

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.timewallet.R
import com.example.timewallet.controls.FormularControl
import com.example.timewallet.controls.ImagePickerControl
import com.example.timewallet.controls.ProfileFragementControl
import com.example.timewallet.controls.WorkRecordControl
import com.example.timewallet.dialogs.DateInputDialog
import com.example.timewallet.dialogs.TimeInputDialog
import com.example.timewallet.record.WorkRecord
import com.example.timewallet.record.WorkRecordToTxt.Companion.saveWorkRecordToFile
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * CaptureFragment
 *
 * Dieses Fragment ermöglicht dem Benutzer, Arbeitszeiten zu erfassen, einschließlich Datum, Start- und Endzeit
 * sowie Sonderfallzeiten. Es enthält auch Eingabefelder für die Auswahl eines Grundes und bietet eine Möglichkeit zum
 * Zurücksetzen und Speichern der erfassten Daten.
 *
 * @author Marco Martins
 * @created 25.10.2023
 */

class CaptureFragment : Fragment() {
    private lateinit var dateInput: TextInputEditText
    private lateinit var startTimeInput: TextInputEditText
    private lateinit var endTimeInput: TextInputEditText
    private lateinit var sonderfallTimeInput: TextInputEditText
    private lateinit var chipGroup: ChipGroup
    private lateinit var profileControl: ProfileFragementControl
    private lateinit var imagePicker: ImagePickerControl

    /**
     * Die Methode wird aufgerufen, um das Fragment zu erstellen und die Benutzeroberfläche
     * sowie die entsprechenden Kontrollen zu initialisieren.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_capture, container, false)
        val profileIcon = view.findViewById<ImageView>(R.id.profile)
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)

        profileControl =
            ProfileFragementControl(requireActivity().supportFragmentManager, bottomNavigationView)
        profileControl.setupProfileIcon(profileIcon)
        imagePicker = ImagePickerControl(this)
        val savedImageFile = imagePicker.getImageFile()

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            if (savedImageFile != null) {
                profileIcon.setImageURI(Uri.fromFile(savedImageFile))
            } else {
                profileIcon.setImageResource(R.drawable.img_white_profile)
            }
        } else {
            if (savedImageFile != null) {
                profileIcon.setImageURI(Uri.fromFile(savedImageFile))
            } else {
                profileIcon.setImageResource(R.drawable.img_profile)
            }
        }

        dateInput = view.findViewById(R.id.dateInput)
        startTimeInput = view.findViewById(R.id.startTimeInput)
        endTimeInput = view.findViewById(R.id.endTimeInput)
        sonderfallTimeInput = view.findViewById(R.id.sonderfallTimeInput)
        chipGroup = view.findViewById(R.id.chipGroup)

        val formularControl = FormularControl()
        formularControl.disableOtherFields(
            startTimeInput,
            endTimeInput,
            sonderfallTimeInput,
            chipGroup
        )

        val zurucksetzenButton = view.findViewById<Button>(R.id.zurücksetzen)
        zurucksetzenButton.setOnClickListener {

            dateInput.text?.clear()
            startTimeInput.text?.clear()
            endTimeInput.text?.clear()
            sonderfallTimeInput.text?.clear()
            chipGroup.clearCheck()
        }

        val speicherButton = view.findViewById<Button>(R.id.speichern)
        speicherButton.setOnClickListener {
            val date = dateInput.text.toString()
            val startTime = startTimeInput.text.toString()
            val endTime = endTimeInput.text.toString()
            val sonderfallTime = sonderfallTimeInput.text.toString()
            val workRecordControl = WorkRecordControl()
            val chipInput = workRecordControl.chipControl(chipGroup)
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val dateFromUser = LocalDate.parse(date, formatter)

            if (date.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Fehler: Geben Sie ein Datum ein!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (dateFromUser.isAfter(LocalDate.now())) {
                Toast.makeText(
                    requireContext(),
                    "Fehler: Das Datum liegt in der Zukunft!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if ((startTime.isEmpty() || endTime.isEmpty()) && (sonderfallTime.isEmpty() && chipInput.isEmpty())) {
                if (startTime.isEmpty() && endTime.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Füllen Sie eins der beiden Formulare aus!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else if (startTime.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Fehler: Geben Sie eine Startzeit an!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else if (endTime.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Fehler: Geben Sie eine Endzeit an!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            }

            if ((sonderfallTime.isEmpty() || chipInput.isEmpty()) && (startTime.isEmpty() && endTime.isEmpty())) {
                if (sonderfallTime.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Fehler: Geben Sie die Stunden an, welche zur Berechnung genutzt werden sollen!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Fehler: Geben Sie einen Grund an!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            }

            val workedHours = sonderfallTimeInput.text.takeUnless { it.isNullOrBlank() }?.toString()
                ?: workRecordControl.workedHoursCalculator(startTimeInput, endTimeInput)

            var fileName = ""
            if (date.isNotEmpty()) {
                try {
                    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    val currentDate = dateFormat.parse(date)

                    val month = SimpleDateFormat("MM", Locale.getDefault()).format(currentDate!!)
                    val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(currentDate)

                    fileName = "work_records_$month-$year.txt"
                } catch (e: ParseException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Speichern nicht möglich!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            val workRecord = WorkRecord(date, startTime, endTime, workedHours, chipInput)
            saveWorkRecordToFile(requireContext(), workRecord, fileName)

            dateInput.text?.clear()
            startTimeInput.text?.clear()
            endTimeInput.text?.clear()
            sonderfallTimeInput.text?.clear()
            chipGroup.clearCheck()
        }

        val dateInputDialog = DateInputDialog()
        dateInputDialog.dateInputDialogOpener(dateInput, parentFragmentManager)

        val timeInputDialog = TimeInputDialog()
        timeInputDialog.timeInputOpener(
            startTimeInput,
            parentFragmentManager,
            "Wählen Sie eine Zeit:"
        )
        timeInputDialog.timeInputOpener(
            endTimeInput,
            parentFragmentManager,
            "Wählen Sie eine Zeit:"
        )
        timeInputDialog.timeInputOpener(
            sonderfallTimeInput,
            parentFragmentManager,
            "Wählen Sie ihre Stunden:"
        )

        return view
    }
}