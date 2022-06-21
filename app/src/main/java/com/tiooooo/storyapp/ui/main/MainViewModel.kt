package com.tiooooo.storyapp.ui.main

import androidx.lifecycle.LiveData
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

    private val _stories = MutableLiveData<ArrayList<StoryViewParam>>()
    private val _storiesState = MutableLiveData<Boolean>()
    private val _storiesError = MutableLiveData<String>()


    val listStories: LiveData<ArrayList<StoryViewParam>> get() = _stories
    val listState: LiveData<Boolean> get() = _storiesState
    val listError: LiveData<String> get() = _storiesError

    fun getStories() = viewModelScope.launch {
        storiesRepositoryContract.getStories().collectLatest {
            when (it) {
                is States.Loading -> _storiesState.value = true
                is States.Success -> {
                    _storiesState.value = false
                    it.data?.let { data -> _stories.value = data }
                }
                is States.Error -> {
                    _storiesState.value = false
                    it.message?.let { error -> _storiesError.value = error }
                }
            }
        }
    }
}