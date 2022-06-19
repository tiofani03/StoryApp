package com.tiooooo.storyapp.di

import com.tiooooo.storyapp.ui.about.AboutViewModel
import com.tiooooo.storyapp.ui.create.CreateStoryViewModel
import com.tiooooo.storyapp.ui.auth.AuthViewModel
import com.tiooooo.storyapp.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { CreateStoryViewModel(get()) }
    viewModel { AboutViewModel(get()) }
}