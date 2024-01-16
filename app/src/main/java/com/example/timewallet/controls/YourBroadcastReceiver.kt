package com.example.timewallet.controls

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class YourBroadcastReceiver : BroadcastReceiver() {
     override fun onReceive(context: Context?, intent: Intent?) {
        val reminderControl = ReminderControl()

        // Hier kannst du auch zusätzliche Logik hinzufügen, bevor du die Benachrichtigung zeigst
        reminderControl.showNotification(
            context!!,
            "Erinnerung",
            "Vergessen Sie nicht Ihre Arbeitszeit zu erfassen!"
        )
    }
}
