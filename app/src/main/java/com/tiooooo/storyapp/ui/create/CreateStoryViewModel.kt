package com.tiooooo.storyapp.ui.create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiooooo.core.contract.StoriesRepositoryContract
import com.tiooooo.core.utils.States
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

class CreateStoryViewModel(
    private val storiesRepositoryContract: StoriesRepositoryContract
) : ViewModel() {

    val createStoriesState = MutableLiveData<Boolean>()
    val createStories = MutableLiveData<Boolean>()
    val createStoriesError = MutableLiveData<String>()
    fun createStories(email: String, image: File) = viewModelScope.launch {
        storiesRepositoryContract.createStories(
            email,
            image
        ).collectLatest {
            when (it) {
                is States.Loading -> createStoriesState.value = true
                is States.Success -> {
                    createStoriesState.value = false
                    it.data?.let { data -> createStories.value = data }
                }
                is States.Error -> {
                    createStoriesState.value = false
                    createStoriesError.value = it.message.orEmpty()
                }
            }
        }
    }
}