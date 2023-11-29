package com.example.timewallet.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.timewallet.R
import com.example.timewallet.controls.ProfileFragementControl
import com.example.timewallet.controls.WorkRecordControl
import com.example.timewallet.dialogs.DateInputDialog
import com.example.timewallet.pdf.WorkRecordListToPDF
import com.example.timewallet.record.WorkRecord
import com.example.timewallet.record.WorkRecordsToList
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalenderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalenderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var profileControl: ProfileFragementControl
    private var selectedStartYear: String = ""
    private var selectedStartMonth: String = ""
    private var selectedEndYear: String = ""
    private var selectedEndMonth: String = ""
    private var concreteStartDate: String = ""
    private var concreteEndDate: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calender, container, false)
        val profileIcon = view.findViewById<ImageView>(R.id.profile)

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)

        profileControl = ProfileFragementControl(requireActivity().supportFragmentManager, bottomNavigationView)
        profileControl.setupProfileIcon(profileIcon)

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            // Dark Mode: Setzen Sie das weiße Icon
            profileIcon.setImageResource(R.drawable.img_white_profile)
        } else {
            // Nicht im Dark Mode: Setzen Sie das reguläre Icon
            profileIcon.setImageResource(R.drawable.img_profile)
        }

        val calender = view.findViewById<CalendarView>(R.id.calenderPick)

        val downloadButtonOne = view.findViewById<Button>(R.id.pdfDownloadZeitraum)
        val dateInputDialog = DateInputDialog()
        val workRecordList = WorkRecordsToList()
        val workRecordPdf = WorkRecordListToPDF()
        var workRecordControl = WorkRecordControl()

        val currentDate = LocalDate.now()
        val currentDateString = currentDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()))
        val currentMonth = currentDate.format(DateTimeFormatter.ofPattern("MM", Locale.getDefault()))
        val currentYear = currentDate.format(DateTimeFormatter.ofPattern("YYYY", Locale.getDefault()))
        val fileName = "work_records_$currentMonth-$currentYear.txt"
        var workRecord : List<WorkRecord> = workRecordList.readWorkRecordsFromFile(requireContext(), fileName)
        val workRecordDataList = workRecordControl.calenderDayView(workRecord,currentDateString)
        val startTime = view.findViewById<TextView>(R.id.startTimeCalender)
        val endTime = view.findViewById<TextView>(R.id.endTimeCalender)
        val hours = view.findViewById<TextView>(R.id.hoursCalender)
        val calenderDetails = view.findViewById<TextView>(R.id.detailsCalender)
        startTime.text = workRecordDataList[0]
        endTime.text = workRecordDataList[1]
        hours.text = workRecordDataList[2]
        calenderDetails.text = workRecordDataList[3]

        calender.setOnDateChangeListener { _, year, month, dayOfMonth ->

            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            val formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()))
            val concreteYear = selectedDate.format(DateTimeFormatter.ofPattern("YYYY", Locale.getDefault()))
            val concreteMonth = selectedDate.format(DateTimeFormatter.ofPattern("MM", Locale.getDefault()))
            val dateString = formattedDate.toString()
            val fileName = "work_records_$concreteMonth-$concreteYear.txt"
            var workRecord : List<WorkRecord> = workRecordList.readWorkRecordsFromFile(requireContext(), fileName)
            val workRecordDataList = workRecordControl.calenderDayView(workRecord,dateString)
            startTime.text = workRecordDataList[0]
            endTime.text = workRecordDataList[1]
            hours.text = workRecordDataList[2]
            calenderDetails.text = workRecordDataList[3]
        }

        downloadButtonOne.setOnClickListener {
            dateInputDialog.dateButtonDialogOpener(parentFragmentManager) { startFormattedDate, endFormattedDate ->
                // Hier kannst du die formatierten Daten verwenden
                val startSimpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val startDate = startSimpleDateFormat.parse(startFormattedDate)
                val endSimpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val endDate = endSimpleDateFormat.parse(endFormattedDate)

                concreteStartDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(startDate)
                concreteEndDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(endDate)
                selectedStartYear =
                    SimpleDateFormat("yyyy", Locale.getDefault()).format(startDate)
                selectedStartMonth =
                    SimpleDateFormat("MM", Locale.getDefault()).format(startDate)

                selectedEndYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(endDate)
                selectedEndMonth = SimpleDateFormat("MM", Locale.getDefault()).format(endDate)

                var monthsBetweenDates = workRecordControl.generateMonthsBetweenDates(startDate, endDate)
                var workRecord : List<WorkRecord> = emptyList()


                for (formattedMonth in monthsBetweenDates) {
                    val fileName = "work_records_$formattedMonth.txt"
                    workRecord = workRecord + workRecordList.readWorkRecordsFromFile(requireContext(), fileName)
                }

                // Hier kannst du die Dateinamen und die formatierten Daten verwenden, um die gewünschte Logik durchzuführen
                workRecordPdf.createPDFPeriod(
                    requireContext(),
                    workRecord,
                    concreteStartDate,
                    concreteEndDate
                )
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
         * @return A new instance of fragment CalenderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalenderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}