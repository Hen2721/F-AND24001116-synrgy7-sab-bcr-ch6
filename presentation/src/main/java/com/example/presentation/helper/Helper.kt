package com.example.presentation.helper

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import com.example.presentation.R
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

object Helper {
    fun showToast(activity: Activity, context: Context, message: String, isSuccess: Boolean) {
        MotionToast.createColorToast(activity,
            if (isSuccess) context.getString(R.string.message_success) else context.getString(R.string.message_error),
            message,
            if (isSuccess) MotionToastStyle.SUCCESS else MotionToastStyle.ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(context, R.font.poppins))
    }

    fun requestPermissions(
        activity: Activity,
        permissions: Array<String>,
        requestCode: Int,
        doIfGranted: () -> Unit = {},
    ) {
        val deniedPermissions = mutableListOf<String>()
        permissions.forEach { permission ->
            if (!isGranted(activity, permission)) {
                deniedPermissions.add(permission)
            }
        }
        if (deniedPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, deniedPermissions.toTypedArray(), requestCode)
        } else {
            doIfGranted()
        }
    }

    fun isGranted(activity: Activity, permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
    }
}