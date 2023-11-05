package com.example.timewallet.controls

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText

class FormularControl {
    fun disableOtherFields(startTimeInput: TextInputEditText, endTimeInput: TextInputEditText,
                           sonderfallTimeInput: TextInputEditText, chipGroup: ChipGroup) {

        fun updateFieldState() {
            val startTimeText = startTimeInput.text?.toString()
            val endTimeText = endTimeInput.text?.toString()
            val sonderfallTimeText = sonderfallTimeInput.text?.toString()
            val chipGroupChecked = chipGroup.checkedChipId

            if (!startTimeText.isNullOrBlank() || !endTimeText.isNullOrBlank()) {
                for (i in 0 until chipGroup.childCount) {
                    val chip = chipGroup.getChildAt(i) as Chip
                    chip.isEnabled = false
                }
                sonderfallTimeInput.isEnabled = false
            } else if (!sonderfallTimeText.isNullOrBlank() || chipGroupChecked != View.NO_ID) {
                startTimeInput.isEnabled = false
                endTimeInput.isEnabled = false
            } else {
                startTimeInput.isEnabled = true
                endTimeInput.isEnabled = true
                sonderfallTimeInput.isEnabled = true
                for (i in 0 until chipGroup.childCount) {
                    val chip = chipGroup.getChildAt(i) as Chip
                    chip.isEnabled = true
                }
            }
        }

        startTimeInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateFieldState()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        endTimeInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateFieldState()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        sonderfallTimeInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateFieldState()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        chipGroup.setOnCheckedStateChangeListener { group, checkedId ->
            updateFieldState()
        }

        // Initialen Zustand festlegen
        updateFieldState()
    }
}