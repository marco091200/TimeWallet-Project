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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CaptureFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CaptureFragment : Fragment() {
    // Deklaration der UI-Elemente
    private lateinit var dateInput: TextInputEditText
    private lateinit var startTimeInput: TextInputEditText
    private lateinit var endTimeInput: TextInputEditText
    private lateinit var sonderfallTimeInput: TextInputEditText
    private lateinit var chipGroup: ChipGroup
    private lateinit var profileControl: ProfileFragementControl
    private lateinit var imagePicker: ImagePickerControl
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_capture, container, false)
        val profileIcon = view.findViewById<ImageView>(R.id.profile)
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)

        profileControl = ProfileFragementControl(requireActivity().supportFragmentManager, bottomNavigationView)
        profileControl.setupProfileIcon(profileIcon)
        //Profile Icon Day and Night Mode
        imagePicker = ImagePickerControl(this)
        val savedImageFile = imagePicker.getImageFile()

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            // Dark Mode: Setzen Sie das weiße Icon
            if (savedImageFile != null) {
                profileIcon.setImageURI(Uri.fromFile(savedImageFile))
            } else {
                profileIcon.setImageResource(R.drawable.img_white_profile)
            }
        } else {
            // Nicht im Dark Mode: Setzen Sie das reguläre Icon
            if (savedImageFile != null) {
                profileIcon.setImageURI(Uri.fromFile(savedImageFile))
            } else {
                profileIcon.setImageResource(R.drawable.img_profile)
            }
        }

        // Referenzierung der UI-Elemente
        dateInput = view.findViewById(R.id.dateInput)
        startTimeInput = view.findViewById(R.id.startTimeInput)
        endTimeInput = view.findViewById(R.id.endTimeInput)
        sonderfallTimeInput = view.findViewById(R.id.sonderfallTimeInput)
        chipGroup = view.findViewById(R.id.chipGroup)

        val formularControl = FormularControl()
        formularControl.disableOtherFields(startTimeInput, endTimeInput, sonderfallTimeInput, chipGroup)

        // Zurücksetz-Button
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
            // Speichern Sie die Daten in die Textdatei
            val date = dateInput.text.toString()
            val startTime = startTimeInput.text.toString()
            val endTime = endTimeInput.text.toString()
            val sonderfallTime = sonderfallTimeInput.text.toString()
            val workRecordControl = WorkRecordControl()
            val chipInput = workRecordControl.chipControl(chipGroup)

            if (date.isEmpty()){
                Toast.makeText(requireContext(), "Fehler: Geben Sie ein Datum ein!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if ((startTime.isEmpty() || endTime.isEmpty()) && (sonderfallTime.isEmpty() && chipInput.isEmpty())) {
                if (startTime.isEmpty() && endTime.isEmpty()){
                    Toast.makeText(
                        requireContext(),
                        "Füllen Sie eins der beiden Formulare aus!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (startTime.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Fehler: Geben Sie eine Startzeit an!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (endTime.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Fehler: Geben Sie eine Endzeit an!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@setOnClickListener
            }

            if ((sonderfallTime.isEmpty() || chipInput.isEmpty()) && (startTime.isEmpty() && endTime.isEmpty())) {
                if (sonderfallTime.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Fehler: Geben Sie die Stunden an, welche zur Berechnung genutzt werden sollen!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Fehler: Geben Sie einen Grund an!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@setOnClickListener
            }

            val workedHours = sonderfallTimeInput.text.takeUnless { it.isNullOrBlank() }?.toString()
                ?: workRecordControl.workedHoursCalculator(startTimeInput, endTimeInput)

            var fileName = ""
            if (date.isNotEmpty()) {
                try {
                    val currentDate = LocalDate.now()
                    val month = currentDate.format(DateTimeFormatter.ofPattern("MM", Locale.getDefault()))
                    val year = currentDate.format(DateTimeFormatter.ofPattern("YYYY", Locale.getDefault()))

                    fileName = "work_records_$month-$year.txt"
                } catch (e: ParseException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Speichern nicht möglich!", Toast.LENGTH_SHORT).show()
                }
            }

            val workRecord = WorkRecord(date, startTime, endTime, workedHours, chipInput)
            saveWorkRecordToFile(requireContext(), workRecord, fileName)

            // Zurücksetzen der UI-Elemente
            dateInput.text?.clear()
            startTimeInput.text?.clear()
            endTimeInput.text?.clear()
            sonderfallTimeInput.text?.clear()
            chipGroup.clearCheck()
        }

        val dateInputDialog = DateInputDialog()
        dateInputDialog.dateInputDialogOpener(dateInput, parentFragmentManager)

        val timeInputDialog = TimeInputDialog()
        timeInputDialog.timeInputOpener(startTimeInput, parentFragmentManager, "Wählen Sie eine Zeit:")
        timeInputDialog.timeInputOpener(endTimeInput, parentFragmentManager, "Wählen Sie eine Zeit:")
        timeInputDialog.timeInputOpener(sonderfallTimeInput, parentFragmentManager, "Wählen Sie ihre Stunden:")

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CaptureFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CaptureFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}