package com.example.timewallet.controls

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.timewallet.R
import java.io.File

/**
 * Verwalten des PDF-Downloads von Arbeitszeit-Daten und Erstellen einer Benachrichtigung nach dem Download.
 * Beim Klicken auf die Benachrichtigung wird der PDF-Report geöffnet.
 *
 * @param context Der Kontext der Anwendung
 * @param fileName Der Name der heruntergeladenen PDF-Datei
 *
 *
 * @author Marco Martins
 * @created 18.12.2023
 */

class DownloadWorkRecordControl(private val context: Context, private val fileName: String) {

    private val notificationId = 1
    private val channelId = "pdf_download"
    private val requestCodePremission = 1

    /**
     * Erstellt eine Benachrichtigung, wenn der Download abgeschlossen ist, und öffnet das heruntergeladene PDF.
     */
    fun createNotificationAndOpenPDF() {
        createNotificationChannel()

        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val absdownloadDir = downloadDir.absolutePath


        val fileDir = "$absdownloadDir/$fileName"
        val fileUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            File(fileDir)
        )
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(fileUri, "application/pdf")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Download abgeschlossen")
            .setContentIntent(pendingIntent)
            .setContentText(fileName)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(context)
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    requestCodePremission
                )
            }
        } else {
            notificationManager.notify(notificationId, builder.build())
        }
    }

    /**
     * Erstellt einen Notification Channel für die Benachrichtigungen über den PDF-Download.
     */
    private fun createNotificationChannel() {
        val name = "PDF Download"
        val descriptionText = "Benachrichtigung zum Download des Arbeitszeit PDFs!"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
