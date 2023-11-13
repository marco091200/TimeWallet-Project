package com.example.timewallet.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.timewallet.R
import com.example.timewallet.pdf.WorkRecordListToPDF
import com.example.timewallet.record.WorkRecordsToList
import java.text.SimpleDateFormat
import java.util.Calendar
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

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            // Dark Mode: Setzen Sie das weiße Icon
            profileIcon.setImageResource(R.drawable.img_white_profile)
        } else {
            // Nicht im Dark Mode: Setzen Sie das reguläre Icon
            profileIcon.setImageResource(R.drawable.img_profile)
        }

        val downloadButton = view.findViewById<Button>(R.id.pdfDownload)
        downloadButton.setOnClickListener {
            val calendarView = view.findViewById<CalendarView>(R.id.calenderPick)

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = calendarView.date

            val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val formattedDate = simpleDateFormat.format(calendar.time)
            val date = simpleDateFormat.parse(formattedDate)

            val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
            val month = SimpleDateFormat("MM", Locale.getDefault()).format(date)
            var fileName = "work_records_$month-$year.txt"

            val workRecordList = WorkRecordsToList()
            val workRecordPdf = WorkRecordListToPDF()
            workRecordPdf.createPDF(requireContext(),workRecordList.readWorkRecordsFromFile(requireContext(), fileName))
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