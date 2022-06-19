package com.tiooooo.storyapp.ui.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiooooo.core.contract.AuthRepositoryContract
import com.tiooooo.core.model.UserModel
import kotlinx.coroutines.launch

class AboutViewModel(private val authRepositoryContract: AuthRepositoryContract) : ViewModel() {
    fun logout() = viewModelScope.launch {
        authRepositoryContract.logout()
    }

    fun getUser() : LiveData<UserModel> = authRepositoryContract.getUser()
}