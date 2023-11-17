package com.example.timewallet.fragments

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.example.timewallet.R
import com.example.timewallet.dialogs.DateInputDialog
import com.example.timewallet.pdf.WorkRecordListToPDF
import com.example.timewallet.record.WorkRecordsToList
import java.text.SimpleDateFormat
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
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var selectedStartYear: String = ""
    private var selectedStartMonth: String = ""
    private var selectedEndYear: String = ""
    private var selectedEndMonth: String = ""
    private var concreteStartDate: String = ""
    private var concreteEndDate: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val profileIcon = view.findViewById<ImageView>(R.id.profile)

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            // Dark Mode: Setzen Sie das weiße Icon
            profileIcon.setImageResource(R.drawable.img_white_profile)
        } else {
            // Nicht im Dark Mode: Setzen Sie das reguläre Icon
            profileIcon.setImageResource(R.drawable.img_profile)
        }

        val downloadButtonOne = view.findViewById<Button>(R.id.pdfDownloadZeitraum)
        val dateInputDialog = DateInputDialog()

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

                    val startFileName = "work_records_$selectedStartMonth-$selectedStartYear.txt"
                    val endFileName = "work_records_$selectedEndMonth-$selectedEndYear.txt"

                    val workRecordList = WorkRecordsToList()
                    val workRecordPdf = WorkRecordListToPDF()

                    // Hier kannst du die Dateinamen und die formatierten Daten verwenden, um die gewünschte Logik durchzuführen
                    workRecordPdf.createPDFPeriod(
                        requireContext(),
                        workRecordList.readWorkRecordsFromFile(requireContext(), startFileName),
                        workRecordList.readWorkRecordsFromFile(requireContext(), endFileName),
                        concreteStartDate,
                        concreteEndDate
                    )
                }
            }
        val downloadButtonTwo = view.findViewById<Button>(R.id.pdfDownloadZeitraum)
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