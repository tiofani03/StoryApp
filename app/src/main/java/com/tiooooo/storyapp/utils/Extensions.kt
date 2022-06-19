package com.tiooooo.storyapp.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

fun getCurrentVersion(context: Context): String {
    val pInfo: PackageInfo?
    var version = ""

    try {
        pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        version = pInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    return version
}