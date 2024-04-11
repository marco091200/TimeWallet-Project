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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("NAME_SHADOWING")
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var profileControl: ProfileFragementControl
    private lateinit var imagePicker: ImagePickerControl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val profileIcon = view.findViewById<ImageView>(R.id.profile)
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigation)

        val userControl = UserControl(requireContext())

        profileControl = ProfileFragementControl(requireActivity().supportFragmentManager, bottomNavigationView)
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

        val combinedWorkRecordsForYear = workRecordControl.combineWorkRecordsForYear(workRecordsForYear)

        val currentMonth = workRecordList.readWorkRecordsFromFile(requireContext(),currentFile)
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
        if (userList?.benutzerName == null || userList.benutzerName == ""){
            showedName.text = "Fügen Sie einen\nBenutzernamen hinzu!"
            showedName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f) // Schriftgröße ändern
        } else {
            showedName.text = userList.benutzerName
        }

        val overTime = view.findViewById<TextView>(R.id.überstunden)
        overTime.text = workRecordControl.overtime(currentMonth, userList?.monatlicheArbeitsstunden)
        val overTimeYear = view.findViewById<TextView>(R.id.überstundenYear)
        overTimeYear.text = workRecordControl.overtimeYear(workRecordsForYear, userList?.monatlicheArbeitsstunden)
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}