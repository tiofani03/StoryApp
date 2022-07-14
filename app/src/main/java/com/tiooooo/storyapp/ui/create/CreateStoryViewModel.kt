package com.tiooooo.storyapp.ui.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiooooo.core.contract.StoriesRepositoryContract
import com.tiooooo.core.utils.States
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File

class CreateStoryViewModel(
    private val storiesRepositoryContract: StoriesRepositoryContract
) : ViewModel() {

    private val _createStoriesState = MutableLiveData<Boolean>()
    private val _createStories = MutableLiveData<Boolean>()
    private val _createStoriesError = MutableLiveData<String>()

    val createStoriesState: LiveData<Boolean> get() = _createStoriesState
    val createStories: LiveData<Boolean> get() = _createStories
    val createStoriesError: LiveData<String> get() = _createStoriesError

    var latitude : Double? = null
    var longitude : Double? = null

    fun createStories(description: String, image: File) = viewModelScope.launch {
        storiesRepositoryContract.createStories(
            description,
            image,
            latitude,
            longitude
        ).collectLatest {
            when (it) {
                is States.Loading -> _createStoriesState.value = true
                is States.Success -> {
                    _createStoriesState.value = false
                    it.data?.let { data -> _createStories.value = data }
                }
                is States.Error -> {
                    _createStoriesState.value = false
                    _createStoriesError.value = it.message.orEmpty()
                }
            }
        }
    }
}