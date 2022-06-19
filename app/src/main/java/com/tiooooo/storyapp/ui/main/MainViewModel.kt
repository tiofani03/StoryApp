package com.tiooooo.storyapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiooooo.core.contract.StoriesRepositoryContract
import com.tiooooo.core.model.StoryViewParam
import com.tiooooo.core.utils.States
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val storiesRepositoryContract: StoriesRepositoryContract,
) :
    ViewModel() {
    val listStories = MutableLiveData<ArrayList<StoryViewParam>>()
    val listState = MutableLiveData<Boolean>()
    val listError = MutableLiveData<String>()

    fun getStories() = viewModelScope.launch {
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
}