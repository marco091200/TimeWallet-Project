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

class ReminderControl {
    private val channelId = "reminder_id"

    fun setDailyNotification(context: Context, timeString: String) {
        createNotificationChannel(context)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Erstelle ein Intent für den Broadcast-Receiver
        val intent = Intent(context, YourBroadcastReceiver::class.java)
        val requestCode = 0
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Konvertiere den übergebenen String in ein Date-Objekt
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = dateFormat.parse(timeString)

        // Setze das Datum für die Benachrichtigung
        val calendar = Calendar.getInstance()
        if (date != null) {
            calendar.time = date
        }

        // Überprüfe, ob die ausgewählte Zeit bereits vergangen ist. Wenn ja, setze sie auf den nächsten Tag.
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        // Setze die wiederholende Benachrichtigung für jeden Tag zur ausgewählten Uhrzeit
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        Toast.makeText(context, "Sie werden täglich um: $timeString Uhr benachrichtigt", Toast.LENGTH_SHORT).show()
    }

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
