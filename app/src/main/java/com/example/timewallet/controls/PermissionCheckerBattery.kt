package com.example.timewallet.controls

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.PowerManager

class PermissionCheckerBattery {
        companion object {
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
