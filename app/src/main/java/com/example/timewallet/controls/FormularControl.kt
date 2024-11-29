package com.example.timewallet.controls

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText

/**
 * Steuert die Aktivierung und Deaktivierung von Formularfeldern basierend auf Benutzereingaben.
 * Wenn Start-/Endzeit oder Sonderfall aktiv sind, werden bestimmte Felder deaktiviert.
 *
 * @author Marco Martins
 * @created 04.11.2023
 */
class FormularControl {

    /**
     * Aktiviert/Deaktiviert Eingabefelder und Chips basierend auf den Eingaben in den Zeitfeldern oder Sonderfällen.
     * Wenn Start-/Endzeit oder Sonderfall ausgefüllt ist, werden andere Felder deaktiviert.
     *
     * @param startTimeInput Das TextInputEditText für die Startzeit
     * @param endTimeInput Das TextInputEditText für die Endzeit
     * @param sonderfallTimeInput Das TextInputEditText für die Sonderfallzeit
     * @param chipGroup Die ChipGroup mit Chips für Sonderfälle
     */

    fun disableOtherFields(
        startTimeInput: TextInputEditText, endTimeInput: TextInputEditText,
        sonderfallTimeInput: TextInputEditText, chipGroup: ChipGroup
    ) {

        /**
         * Diese Funktion aktualisiert den Zustand der Felder basierend auf den aktuellen Eingaben.
         */
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

        chipGroup.setOnCheckedStateChangeListener { _, _ ->
            updateFieldState()
        }

        updateFieldState()
    }
}