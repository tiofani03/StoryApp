package com.tiooooo.core.utils.extensions

import android.text.TextUtils
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

fun CharSequence.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
        .matches()
}


fun String.toDate(format: String? = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"): Date {
    return try {
        val formatter = SimpleDateFormat(format, Locale("id", "ID"))
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        formatter.parse(this) ?: Date()
    } catch (e: Exception) {
        Timber.e("Failed to parse date")
        Date()
    }
}

fun Date.toDateString(format: String? = "dd MMM HH:mm"): String {
    Timber.d("toDateString: $this")
    val formatter = SimpleDateFormat(format, Locale("id", "ID"))
    return formatter.format(this) ?: ""
}