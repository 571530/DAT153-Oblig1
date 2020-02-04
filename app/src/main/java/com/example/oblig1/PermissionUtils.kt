package com.example.oblig1

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import android.Manifest.permission
import android.Manifest.permission.READ_EXTERNAL_STORAGE




private fun hasPermissions(activity: Activity, permissions: Array<String>): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var res = true
        for (perm in permissions) {
            val result = activity.checkSelfPermission(perm)
            res = res && result == PackageManager.PERMISSION_GRANTED
        }
        return res
    }
    return false
}

fun requirePermissions(activity: Activity, permissions: Array<String>): Boolean {
    return if (hasPermissions(activity, permissions)) {
        true
    }
    else {
        try {
            ActivityCompat.requestPermissions(activity, permissions, 1)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

    }
}