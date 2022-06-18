package com.tiooooo.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiooooo.core.contract.AuthRepositoryContract
import com.tiooooo.core.contract.StoriesRepositoryContract
import com.tiooooo.core.model.UserModel
import com.tiooooo.core.model.StoriesViewParam
import com.tiooooo.core.utils.States
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val storiesRepositoryContract: StoriesRepositoryContract,
    private val authRepositoryContract: AuthRepositoryContract
) :
    ViewModel() {
    val listStories = MutableLiveData<ArrayList<StoriesViewParam>>()
    val listState = MutableLiveData<Boolean>()
    val listError = MutableLiveData<String>()

    fun getListStories() = viewModelScope.launch {
        storiesRepositoryContract.getStories().collectLatest {
            when (it) {
                is States.Loading -> listState.value = true
                is States.Success -> {
                    listState.value = false
                    it.data?.let { data -> listStories.value = data }
                }
                is States.Error -> {
                    listState.value = false
                    it.message?.let { error -> listError.value = error }
                }
            }
        }
    }

    fun getUser(): LiveData<UserModel> = authRepositoryContract.getUser()


    fun logout() = viewModelScope.launch {
        authRepositoryContract.logout()
    }
}