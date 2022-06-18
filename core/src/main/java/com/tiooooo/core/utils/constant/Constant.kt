package com.tiooooo.core.utils.constant

import com.tiooooo.core.BuildConfig


object Constant {
    const val HOST = BuildConfig.HOST
    const val AUTH_HEADER = "Authorization"
    const val USER_AGENT_HEADER = "User-Agent"
    const val DATE_FORMAT = "yyyy-MM-dd"
    const val LONG_DATE = "yyyy-mm-dd HH:mm:ss"
    const val BASE_URL = "$HOST/v1/"
}