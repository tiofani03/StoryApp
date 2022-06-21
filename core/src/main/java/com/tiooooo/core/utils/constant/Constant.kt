package com.tiooooo.core.utils.constant

import com.tiooooo.core.BuildConfig


object Constant {
    private const val HOST = BuildConfig.HOST
    const val AUTH_HEADER = "Authorization"
    const val USER_AGENT_HEADER = "User-Agent"
    const val BASE_URL = "$HOST/v1/"
}