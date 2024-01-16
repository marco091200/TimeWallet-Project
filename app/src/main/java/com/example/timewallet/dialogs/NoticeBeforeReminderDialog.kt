package com.example.timewallet.dialogs

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NoticeBeforeReminderDialog {
    companion object {
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

        private fun openAppSettings(context: Context) {
            val intent = Intent()
            intent.action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = Uri.fromParts("package", context.packageName, null)
            context.startActivity(intent)
        }
    }
}
