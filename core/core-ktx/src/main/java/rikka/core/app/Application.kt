package rikka.core.app

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build

val Application.processNameCompat: String
    get() {
        return if (Build.VERSION.SDK_INT >= 28) Application.getProcessName() else {
            try {
                @SuppressLint("PrivateApi") val activityThread = Class.forName("android.app.ActivityThread")
                @SuppressLint("DiscouragedPrivateApi") val method = activityThread.getDeclaredMethod("currentProcessName")
                (method.invoke(null) as String)
            } catch (e: Throwable) {
                packageName
            }
        }
    }