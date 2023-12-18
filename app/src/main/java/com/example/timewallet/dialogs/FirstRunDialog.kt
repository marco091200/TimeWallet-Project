package com.example.timewallet.dialogs

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FirstRunDialog {
    companion object{
        fun displayFirstRunDialog(context: Context, callback: (accepted: Boolean) -> Unit) {
            val builder = MaterialAlertDialogBuilder(context)
            builder.setTitle("Herzlich Willkommen")
            builder.setMessage("Tauchen Sie ein in die Zukunft der Zeiterfassung mit unserer App.\nIhre Zeit, Ihr Erfolg!")
            builder.setPositiveButton("OK") { _, _ ->
                callback(true)
            }
            builder.setCancelable(false)
            val dialog = builder.create()
            dialog.show()
        }
    }
}