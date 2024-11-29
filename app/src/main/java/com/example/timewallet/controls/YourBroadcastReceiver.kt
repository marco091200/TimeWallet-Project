package com.example.timewallet.controls

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Ein BroadcastReceiver, der auf empfangene Broadcasts reagiert.
 * In diesem Fall wird eine Benachrichtigung angezeigt, wenn der empfangene Broadcast ausgelöst wird.
 *
 * @author Marco Martins
 * @created 02.01.2024
 */
class YourBroadcastReceiver : BroadcastReceiver() {

    /**
     * Wird aufgerufen, wenn ein Broadcast empfangen wird.
     * Hier wird eine Benachrichtigung angezeigt, um den Benutzer an die Zeiterfassung zu erinnern.
     *
     * @param context Der Kontext, in dem der Broadcast empfangen wurde.
     * @param intent Das Intent, das den Broadcast ausgelöst hat.
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        val reminderControl = ReminderControl()

        reminderControl.showNotification(
            context!!,
            "Erinnerung",
            "Vergessen Sie nicht Ihre Arbeitszeit zu erfassen!"
        )
    }
}
