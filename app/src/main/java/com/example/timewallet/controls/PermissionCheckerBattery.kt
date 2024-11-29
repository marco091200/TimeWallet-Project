package com.example.timewallet.controls

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.PowerManager

/**
 * Die `PermissionCheckerBattery`-Klasse prüft, ob die Berechtigung zum Ignorieren von Batterieoptimierungen erteilt wurde.
 *
 * @author Marco Martins
 * @created 16.01.2024
 */
class PermissionCheckerBattery {
    companion object {
        /**
         * Überprüft, ob die App die Berechtigung hat, Batterieoptimierungen zu ignorieren.
         *
         * @param context Der Kontext der Anwendung.
         * @return `true` wenn die Berechtigung erteilt wurde, sonst `false`.
         */
        @SuppressLint("ObsoleteSdkInt")
        fun isBackgroundActivityPermissionGranted(context: Context): Boolean {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return powerManager.isIgnoringBatteryOptimizations(context.packageName)
            }
            return true
        }
    }
}
