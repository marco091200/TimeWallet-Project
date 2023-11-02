package com.example.timewallet.Fragments

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.example.timewallet.R
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import java.text.SimpleDateFormat
import java.util.Date
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

        //Profile Icon Day and Night Mode
        val profileIcon = view.findViewById<ImageView>(R.id.profile)
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            // Dark Mode: Setzen Sie das weiße Icon
            profileIcon.setImageResource(R.drawable.img_white_profile)
        } else {
            // Nicht im Dark Mode: Setzen Sie das reguläre Icon
            profileIcon.setImageResource(R.drawable.img_profile)
        }

        // Referenzierung der UI-Elemente
        dateInput = view.findViewById(R.id.dateInput)
        startTimeInput = view.findViewById(R.id.startTimeInput)
        endTimeInput = view.findViewById(R.id.endTimeInput)
        sonderfallTimeInput = view.findViewById(R.id.sonderfallTimeInput)

        // Zurücksetz-Button
        val zurucksetzenButton = view.findViewById<Button>(R.id.zurücksetzen)
        zurucksetzenButton.setOnClickListener {

            dateInput.text?.clear()
            startTimeInput.text?.clear()
            endTimeInput.text?.clear()
            sonderfallTimeInput.text?.clear()

            val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroup)
            chipGroup.clearCheck()
        }

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Wählen Sie eine Datum aus:")
            .build()

        dateInput.setOnClickListener {
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

        val startTimePicker = MaterialTimePicker
            .Builder()
            .setTitleText("Wählen Sie eine Zeit:")
            .build()

        startTimeInput.setOnClickListener {
            startTimePicker.show(parentFragmentManager, "timePicker")
            startTimePicker.addOnPositiveButtonClickListener {
                val selectedHour = startTimePicker.hour
                val selectedMinute = startTimePicker.minute
                val selectedTimeStart = String.format("%02d:%02d", selectedHour, selectedMinute)
                startTimeInput.setText(selectedTimeStart)
            }
        }

        val endTimePicker = MaterialTimePicker
            .Builder()
            .setTitleText("Wählen Sie eine Zeit:")
            .build()

        endTimeInput.setOnClickListener {
            endTimePicker.show(parentFragmentManager, "timePicker")
            endTimePicker.addOnPositiveButtonClickListener {
                val selectedHour = endTimePicker.hour
                val selectedMinute = endTimePicker.minute
                val selectedTimeEnd = String.format("%02d:%02d", selectedHour, selectedMinute)
                endTimeInput.setText(selectedTimeEnd)
            }
        }

        val sonderfallTimePicker = MaterialTimePicker
            .Builder()
            .setTitleText("Wählen Sie eine Zeit:")
            .build()

        sonderfallTimeInput.setOnClickListener {
            sonderfallTimePicker.show(parentFragmentManager, "timePicker")
            sonderfallTimePicker.addOnPositiveButtonClickListener {
                val selectedHour = sonderfallTimePicker.hour
                val selectedMinute = sonderfallTimePicker.minute
                val selectedTimeSonderfall = String.format("%02d:%02d", selectedHour, selectedMinute)
                sonderfallTimeInput.setText(selectedTimeSonderfall)
            }
        }

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