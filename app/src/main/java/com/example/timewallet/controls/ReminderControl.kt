package com.example.timewallet.controls

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.timewallet.R
import com.example.timewallet.main.MainActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

/**
 * Die `ReminderControl`-Klasse verwaltet tägliche Erinnerungen und Benachrichtigungen.
 * Sie nutzt `AlarmManager`, um Benachrichtigungen zu setzen, und `NotificationManager`, um diese anzuzeigen.
 *
 * @author Marco Martins
 * @created 27.12.2023
 */
class ReminderControl {
    private val channelId = "reminder_id"

    /**
     * Setzt eine tägliche Erinnerung, die um die angegebene Zeit ausgelöst wird.
     * Wenn die angegebene Zeit bereits vergangen ist, wird die Erinnerung für den nächsten Tag gesetzt.
     *
     * @param context Der Kontext der Anwendung, um auf Systemdienste zuzugreifen.
     * @param timeString Die Uhrzeit, zu der die Benachrichtigung täglich gesendet werden soll, im Format "HH:mm".
     */

    fun setDailyNotification(context: Context, timeString: String) {
        createNotificationChannel(context)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, YourBroadcastReceiver::class.java)
        val requestCode = 0
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = dateFormat.parse(timeString)

        val calendar = Calendar.getInstance(TimeZone.getDefault())
        if (date != null) {
            calendar.time = date
        }

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        Toast.makeText(context, "Benachrichtigung gesetzt für: $timeString Uhr", Toast.LENGTH_SHORT)
            .show()
    }

    /**
     * Erstellt einen Benachrichtigungskanal, falls er noch nicht existiert.
     * Wird benötigt, um Benachrichtigungen auf Geräten mit Android 8.0 (API Level 26) und höher korrekt anzuzeigen.
     *
     * @param context Der Kontext der Anwendung, um auf den NotificationManager zuzugreifen.
     */
    private fun createNotificationChannel(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "ReminderChannel",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        notificationManager.createNotificationChannel(channel)
    }

    /**
     * Zeigt eine einmalige Benachrichtigung an.
     * Diese Benachrichtigung öffnet die `MainActivity` beim Klicken.
     *
     * @param context Der Kontext der Anwendung, um auf den NotificationManager zuzugreifen.
     * @param title Der Titel der Benachrichtigung.
     * @param content Der Inhalt der Benachrichtigung.
     */
    fun showNotification(context: Context, title: String, content: String) {
        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(1, builder.build())
    }
}
