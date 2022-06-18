package com.tiooooo.core.di

import com.tiooooo.core.contract.AuthRepositoryContract
import com.tiooooo.core.contract.StoriesRepositoryContract
import com.tiooooo.core.data.local.UserPreference
import com.tiooooo.core.data.remote.network.AuthService
import com.tiooooo.core.data.remote.network.StoriesService
import com.tiooooo.core.data.repo.AuthRepository
import com.tiooooo.core.data.repo.StoriesRepository
import com.tiooooo.core.utils.constant.Constant.BASE_URL
import org.koin.dsl.module

val localModule = module {
    single { UserPreference(get()) }
}

var retrofitModule = module {
    single { createHttpLoggingInterceptor() }
    single { createOkHttpClient(get(), get(), get()) }
    single { createRetrofit(get(), BASE_URL).create(AuthService::class.java) }
    single { createRetrofit(get(), BASE_URL).create(StoriesService::class.java) }
}

var repositoryModule = module {
    factory<AuthRepositoryContract> { AuthRepository(get(), get()) }
    factory<StoriesRepositoryContract> { StoriesRepository(get()) }
}
