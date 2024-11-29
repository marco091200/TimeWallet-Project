package com.example.timewallet.dialogs

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Diese Klasse stellt einen Dialog zur Verfügung, der beim ersten Start der App angezeigt wird.
 * Der Dialog begrüßt den Benutzer und bietet eine Schaltfläche zum Bestätigen der Anzeige.
 *
 *
 * @author Marco Martins
 * @created 26.10.2023
 */
class FirstRunDialog {
    companion object {
        /**
         * Zeigt einen Willkommensdialog an, der beim ersten Start der App angezeigt wird.
         * Der Dialog erklärt kurz die Funktionalität der App und fordert den Benutzer auf, den Dialog zu bestätigen.
         *
         * @param context Der Kontext, in dem der Dialog angezeigt wird.
         * @param callback Eine Callback-Funktion, die mit einem `true`-Wert aufgerufen wird,
         * wenn der Benutzer den Dialog mit der "OK"-Schaltfläche bestätigt.
         */
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