package com.example.timewallet.fragments

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.timewallet.R
import com.example.timewallet.controls.DownloadWorkRecordControl
import com.example.timewallet.controls.ImagePickerControl
import com.example.timewallet.controls.ProfileFragementControl
import com.example.timewallet.controls.WorkRecordControl
import com.example.timewallet.dialogs.DateInputDialog
import com.example.timewallet.pdf.WorkRecordListToPDF
import com.example.timewallet.record.WorkRecord
import com.example.timewallet.record.WorkRecordToTxt.Companion.deleteRecordByDate
import com.example.timewallet.record.WorkRecordsToList
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * CalenderFragment ist ein Fragment, das die Kalenderansicht und die Verwaltung der Arbeitszeitaufzeichnungen für den Benutzer ermöglicht.
 * Es bietet Funktionen zum Anzeigen von Arbeitszeiten für bestimmte Tage, Löschen von Aufzeichnungen und Herunterladen von PDF-Berichten.
 *
 * @author Marco Martins
 * @created 25.10.2023
 */
@Suppress("NAME_SHADOWING")
class CalenderFragment : Fragment() {
    private lateinit var profileControl: ProfileFragementControl
    private var selectedStartYear: String = ""
    private var selectedStartMonth: String = ""
    private var selectedEndYear: String = ""
    private var selectedEndMonth: String = ""
    private var concreteStartDate: String = ""
    private var concreteEndDate: String = ""
    private lateinit var imagePicker: ImagePickerControl

    /**
     * Diese Methode wird aufgerufen, wenn das Fragment erstellt wird und zeigt das Layout an.
     * Es wird die Benutzeroberfläche mit den entsprechenden Daten geladen und die notwendigen Listener gesetzt.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calender, container, false)
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


        val calender = view.findViewById<CalendarView>(R.id.calenderPick)

        val downloadButtonOne = view.findViewById<Button>(R.id.pdfDownload)
        val deleteButton = view.findViewById<Button>(R.id.deleteData)
        val dateInputDialog = DateInputDialog()
        val workRecordList = WorkRecordsToList()
        val workRecordPdf = WorkRecordListToPDF()
        val workRecordControl = WorkRecordControl()

        val currentDate = LocalDate.now()
        val currentDateString =
            currentDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()))
        val currentMonth =
            currentDate.format(DateTimeFormatter.ofPattern("MM", Locale.getDefault()))
        val currentYear =
            currentDate.format(DateTimeFormatter.ofPattern("YYYY", Locale.getDefault()))
        val fileName = "work_records_$currentMonth-$currentYear.txt"
        val workRecord: List<WorkRecord> =
            workRecordList.readWorkRecordsFromFile(requireContext(), fileName)
        val workRecordDataList = workRecordControl.calenderDayView(workRecord, currentDateString)
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
            val formattedDate =
                selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()))
            val concreteYear =
                selectedDate.format(DateTimeFormatter.ofPattern("YYYY", Locale.getDefault()))
            val concreteMonth =
                selectedDate.format(DateTimeFormatter.ofPattern("MM", Locale.getDefault()))
            val dateString = formattedDate.toString()
            val fileName = "work_records_$concreteMonth-$concreteYear.txt"
            val workRecord: List<WorkRecord> =
                workRecordList.readWorkRecordsFromFile(requireContext(), fileName)
            val workRecordDataList = workRecordControl.calenderDayView(workRecord, dateString)
            startTime.text = workRecordDataList[0]
            endTime.text = workRecordDataList[1]
            hours.text = workRecordDataList[2]
            calenderDetails.text = workRecordDataList[3]

            deleteButton.setOnClickListener {
                deleteRecordByDate(requireContext(), fileName, dateString)
                reload()
            }
        }

        deleteButton.setOnClickListener {
            deleteRecordByDate(requireContext(), fileName, currentDateString)
            reload()
        }

        downloadButtonOne.setOnClickListener {
            dateInputDialog.dateButtonDialogOpener(parentFragmentManager) { startFormattedDate, endFormattedDate ->
                // Hier kannst du die formatierten Daten verwenden
                val startSimpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val startDate = startSimpleDateFormat.parse(startFormattedDate)
                val endSimpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val endDate = endSimpleDateFormat.parse(endFormattedDate)

                concreteStartDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(
                    startDate!!
                )
                concreteEndDate =
                    SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(endDate!!)
                selectedStartYear =
                    SimpleDateFormat("yyyy", Locale.getDefault()).format(startDate)
                selectedStartMonth =
                    SimpleDateFormat("MM", Locale.getDefault()).format(startDate)

                selectedEndYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(endDate)
                selectedEndMonth = SimpleDateFormat("MM", Locale.getDefault()).format(endDate)

                val monthsBetweenDates =
                    workRecordControl.generateMonthsBetweenDates(startDate, endDate)
                var workRecord: List<WorkRecord> = emptyList()


                for (formattedMonth in monthsBetweenDates) {
                    val fileName = "work_records_$formattedMonth.txt"
                    workRecord = workRecord + workRecordList.readWorkRecordsFromFile(
                        requireContext(),
                        fileName
                    )
                }

                // Hier kannst du die Dateinamen und die formatierten Daten verwenden, um die gewünschte Logik durchzuführen
                val fileName = workRecordPdf.createPDFPeriod(
                    requireContext(),
                    workRecord,
                    concreteStartDate,
                    concreteEndDate
                )

                val downloadWorkRecordControl =
                    DownloadWorkRecordControl(requireContext(), fileName)
                downloadWorkRecordControl.createNotificationAndOpenPDF()
            }
        }


        return view
    }

    /**
     * Diese Methode wird aufgerufen, um die Ansicht nach dem Löschen von Arbeitsaufzeichnungen neu zu laden.
     * Sie liest die aktuellen Arbeitsaufzeichnungen und aktualisiert die Textansichten im Kalender.
     */
    private fun reload() {
        val workRecordList = WorkRecordsToList()
        val workRecordControl = WorkRecordControl()
        val currentDate = LocalDate.now()
        val currentDateString =
            currentDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()))
        val currentMonth =
            currentDate.format(DateTimeFormatter.ofPattern("MM", Locale.getDefault()))
        val currentYear =
            currentDate.format(DateTimeFormatter.ofPattern("YYYY", Locale.getDefault()))
        val fileName = "work_records_$currentMonth-$currentYear.txt"
        val view = view ?: return
        val workRecord: List<WorkRecord> =
            workRecordList.readWorkRecordsFromFile(requireContext(), fileName)
        val workRecordDataList = workRecordControl.calenderDayView(workRecord, currentDateString)
        val startTime = view.findViewById<TextView>(R.id.startTimeCalender)
        val endTime = view.findViewById<TextView>(R.id.endTimeCalender)
        val hours = view.findViewById<TextView>(R.id.hoursCalender)
        val calenderDetails = view.findViewById<TextView>(R.id.detailsCalender)

        startTime.text = workRecordDataList[0]
        endTime.text = workRecordDataList[1]
        hours.text = workRecordDataList[2]
        calenderDetails.text = workRecordDataList[3]
    }
}