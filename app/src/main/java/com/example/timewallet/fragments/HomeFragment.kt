package com.example.timewallet.fragments

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.timewallet.R
import com.example.timewallet.controls.ImagePickerControl
import com.example.timewallet.controls.ProfileFragementControl
import com.example.timewallet.controls.UserControl
import com.example.timewallet.controls.WorkRecordControl
import com.example.timewallet.record.WorkRecord
import com.example.timewallet.record.WorkRecordsToList
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * HomeFragment
 *
 * Dieses Fragment stellt die Hauptansicht des Benutzers dar, einschließlich Profilbild,
 * Arbeitsstunden und eine Übersicht der Arbeitszeit für den aktuellen Monat und das Jahr.
 * Es ermöglicht es dem Benutzer, grundlegende Informationen wie Arbeitsstunden, Urlaubs- und Krankheitstage anzuzeigen.*
 *
 * @author Marco Martins
 * @created 25.10.2023
 */
@Suppress("NAME_SHADOWING")
class HomeFragment : Fragment() {
    private lateinit var profileControl: ProfileFragementControl
    private lateinit var imagePicker: ImagePickerControl

    /**
     * Initialisiert das Fragment, lädt das Layout und konfiguriert alle benötigten UI-Elemente und Logik.
     */
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val profileIcon = view.findViewById<ImageView>(R.id.profile)
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)

        val userControl = UserControl(requireContext())

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

        val workRecordList = WorkRecordsToList()
        val workRecordControl = WorkRecordControl()

        val month = SimpleDateFormat("MM", Locale.getDefault()).format(Date())
        val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())
        val currentFile = "work_records_$month-$year.txt"

        val workRecordsForYear = mutableListOf<List<WorkRecord>>()
        for (i in 1..12) {
            val month = String.format("%02d", i)
            val currentFile = "work_records_$month-$year.txt"
            val monthRecords = workRecordList.readWorkRecordsFromFile(requireContext(), currentFile)
            workRecordsForYear.add(monthRecords)
        }

        val combinedWorkRecordsForYear =
            workRecordControl.combineWorkRecordsForYear(workRecordsForYear)

        val currentMonth = workRecordList.readWorkRecordsFromFile(requireContext(), currentFile)
        val hoursWorked = view.findViewById<TextView>(R.id.monatlicheArbeitsstunden)
        val krank = view.findViewById<TextView>(R.id.krankeTage)
        val ruhetag = view.findViewById<TextView>(R.id.ruhetage)
        val urlaub = view.findViewById<TextView>(R.id.urlaub)
        hoursWorked.text = workRecordControl.hoursMonth(currentMonth)
        krank.text = workRecordControl.sickCounter(currentMonth)
        ruhetag.text = workRecordControl.restDayCounter(currentMonth)
        urlaub.text = workRecordControl.vacationCounter(currentMonth)

        val hoursWorkedYear = view.findViewById<TextView>(R.id.monatlicheArbeitsstundenYear)
        val krankYear = view.findViewById<TextView>(R.id.krankeTageYear)
        val ruhetagYear = view.findViewById<TextView>(R.id.ruhetageYear)
        val urlaubYear = view.findViewById<TextView>(R.id.urlaubYear)
        hoursWorkedYear.text = workRecordControl.hoursMonth(combinedWorkRecordsForYear)
        krankYear.text = workRecordControl.sickCounter(combinedWorkRecordsForYear)
        ruhetagYear.text = workRecordControl.restDayCounter(combinedWorkRecordsForYear)
        urlaubYear.text = workRecordControl.vacationCounter(combinedWorkRecordsForYear)

        val showedName = view.findViewById<TextView>(R.id.angezeigterBenutzerName)
        val userList = userControl.readUserFromTxt()
        if (userList?.benutzerName == null || userList.benutzerName == "") {
            showedName.text = "Fügen Sie einen\nBenutzernamen hinzu!"
            showedName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        } else {
            showedName.text = userList.benutzerName
        }

        val overTime = view.findViewById<TextView>(R.id.überstunden)
        overTime.text = workRecordControl.overtime(currentMonth, userList?.monatlicheArbeitsstunden)
        val overTimeYear = view.findViewById<TextView>(R.id.überstundenYear)
        overTimeYear.text =
            workRecordControl.overtimeYear(workRecordsForYear, userList?.monatlicheArbeitsstunden)
        return view
    }
}