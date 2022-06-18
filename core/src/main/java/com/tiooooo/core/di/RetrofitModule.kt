package com.tiooooo.core.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.tiooooo.core.data.local.UserPreference
import com.tiooooo.core.utils.constant.Constant
import com.tiooooo.core.utils.network.NetworkUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun createRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun createOkHttpClient(
    loggingInterceptor: HttpLoggingInterceptor,
    context: Context,
    userPreference: UserPreference
): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(createSessionRequestInterceptor(context, userPreference))
        .addInterceptor(loggingInterceptor)
        .addInterceptor(createChuckerInterceptor(context))
        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .build()
}

fun createHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return loggingInterceptor
}

fun createSessionRequestInterceptor(context: Context, userPreference: UserPreference): Interceptor {
    return Interceptor { chain ->
        val accessToken = runBlocking { userPreference.getToken().first() }
        if (accessToken.isBlank()) {
            chain.proceed(chain.request())
        } else {
            val request = chain.request().newBuilder()
                .addHeader(Constant.USER_AGENT_HEADER, NetworkUtils.userAgent(context))
                .addHeader(Constant.AUTH_HEADER, "Bearer $accessToken").build()
            chain.proceed(request)
        }
    }
}

fun createChuckerInterceptor(context: Context): ChuckerInterceptor {
    val chuckerCollector = ChuckerCollector(
        context = context,
        showNotification = true,
        retentionPeriod = RetentionManager.Period.ONE_HOUR
    )

    return ChuckerInterceptor.Builder(context)
        .collector(chuckerCollector)
        .maxContentLength(2500_000L)
        .redactHeaders("Auth-Token", "Bearer")
        .build()
}