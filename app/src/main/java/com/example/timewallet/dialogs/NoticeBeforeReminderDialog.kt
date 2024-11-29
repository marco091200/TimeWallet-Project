package com.example.timewallet.dialogs

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Diese Klasse zeigt einen Dialog an, der den Benutzer darüber informiert,
 * dass die App im Hintergrund aktiv bleiben muss, um Benachrichtigungen zu empfangen.
 * Sie bietet dem Benutzer die Möglichkeit, zu den App-Einstellungen zu gelangen, um diese Berechtigung zu gewähren.
 *
 * @author Marco Martins
 * @created 16.01.2024
 */
class NoticeBeforeReminderDialog {
    companion object {
        /**
         * Zeigt einen Dialog an, der den Benutzer darüber informiert, dass die App
         * im Hintergrund aktiv bleiben muss, um Benachrichtigungen zu erhalten.
         * Bietet dem Benutzer die Möglichkeit, die App-Einstellungen zu öffnen.
         *
         * @param context Der Kontext, in dem der Dialog angezeigt wird.
         */
        fun displayBackgroundActivityDialog(context: Context) {
            val builder = MaterialAlertDialogBuilder(context)
            builder.setTitle("Hinweis!")
            builder.setMessage("Um sicherzustellen, dass Sie alle Benachrichtigungen erhalten, müssen Sie der App erlauben, im Hintergrund aktiv zu sein.")
            builder.setPositiveButton("Erlauben") { _, _ ->
                openAppSettings(context)
            }
            builder.setNegativeButton("Ablehnen") { _, _ ->
            }
            val dialog = builder.create()
            dialog.show()
        }

        /**
         * Öffnet die App-Einstellungen, damit der Benutzer Berechtigungen für die App verwalten kann.
         *
         * @param context Der Kontext, der verwendet wird, um die App-Einstellungen zu öffnen.
         */
        private fun openAppSettings(context: Context) {
            val intent = Intent()
            intent.action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = Uri.fromParts("package", context.packageName, null)
            context.startActivity(intent)
        }
    }
}
