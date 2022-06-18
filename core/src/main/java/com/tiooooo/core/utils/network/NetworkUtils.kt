package com.tiooooo.core.utils.network

import android.content.Context
import android.os.Build
import androidx.core.content.pm.PackageInfoCompat
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*

object NetworkUtils {
    fun userAgent(context: Context): String {
        val manufacturer =
            Build.MANUFACTURER.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        val model = Build.MODEL
        val sdk = Build.VERSION.SDK_INT
        val release = Build.VERSION.RELEASE
        val info = context.packageManager.getPackageInfo(context.packageName, 0)
        val pkg = context.packageName
            .split('.')
            .last()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        val version = info.versionName
        val code = PackageInfoCompat.getLongVersionCode(
            context.packageManager.getPackageInfo(
                context.packageName,
                0
            )
        )
        val lang = Locale.getDefault().toString()
        return "Android $release ($sdk)/$manufacturer $model/$pkg $version ($code)/$lang"
    }

    fun Exception.getErrorMessage(): String {
        val defaultErrorMsg =
            "Unable to connect with server. Please check your connection or try again later."

        return when (this) {
            is HttpException -> {
                this.getErrorMessage()
            }
            is UnknownHostException -> {
                defaultErrorMsg
            }
            is SocketTimeoutException -> {
                defaultErrorMsg
            }
            is ConnectException -> {
                defaultErrorMsg
            }
            else -> {
                var message = this.message
                if (message.isNullOrBlank()) {
                    message = "Unknown error."
                }
                message
            }
        }
    }

    fun HttpException.getErrorMessage(): String {
        var message = ""
        this.response()?.errorBody()?.string()?.let {
            try {
                val obj = JSONObject(it)
                if (message.isEmpty()) {
                    if (obj.has("message")) {
                        message = obj.getString("message")
                    }
                }
                if (message.isEmpty()) {
                    message = it
                }
            } catch (e: JSONException) {
                message = it
            } catch (ex: Exception) {
                message = ex.getErrorMessage()
            }
        }
        if (message.isBlank()) {
            message = "Unknown error."
        }

        if (message.startsWith("[") && message.endsWith("]")) {
            try {
                val messageList = Gson().fromJson(message, mutableListOf<String>().javaClass)
                message = messageList.joinToString(", ")
            } catch (e: Exception) {
                Timber.i("failed to convert array as string")
            }
        }

        return message
    }
}