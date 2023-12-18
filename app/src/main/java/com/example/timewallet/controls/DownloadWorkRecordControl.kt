package com.example.timewallet.controls

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.timewallet.R

class DownloadWorkRecordControl(private val context: Context, private val fileName: String) {

    private val notificationId = 1
    private val CHANNEL_ID = "pdf_download"
    private val REQUEST_CODE_PERMISSION = 1

    fun createNotificationAndOpenPDF() {
        createNotificationChannel()

        val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val absdownloadDir = downloadDir.absolutePath


        val fileDir = "$absdownloadDir/$fileName"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(fileDir), "application/pdf")

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
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
                    REQUEST_CODE_PERMISSION
                )
            }
        } else {
            notificationManager.notify(notificationId, builder.build())
        }
    }

    private fun createNotificationChannel() {
        val name = "PDF Download"
        val descriptionText = "Benachrichtigung zum Download des Arbeitszeit PDFs!"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
