package com.tiooooo.storyapp.di

import com.tiooooo.storyapp.ui.add.CreateStoryViewModel
import com.tiooooo.storyapp.ui.auth.AuthViewModel
import com.tiooooo.storyapp.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthViewModel(get(), get()) }
    viewModel { MainViewModel(get(), get()) }
    viewModel { CreateStoryViewModel(get()) }
}