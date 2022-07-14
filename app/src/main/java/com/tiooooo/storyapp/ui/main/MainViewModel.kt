package com.tiooooo.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tiooooo.core.contract.StoriesRepositoryContract
import com.tiooooo.core.model.StoryViewParam
import com.tiooooo.core.utils.States
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val storiesRepositoryContract: StoriesRepositoryContract,
) :
    ViewModel() {
    private val _stories = MutableLiveData<PagingData<StoryViewParam>>()
    val listStories : LiveData<PagingData<StoryViewParam>> get() = _stories

    fun getStories() = viewModelScope.launch {
        storiesRepositoryContract.getStories().cachedIn(viewModelScope).collectLatest {
            _stories.value = it
        }
    }


    private val _storiesMap = MutableLiveData<List<StoryViewParam>>()
    val listStoriesMap : LiveData<List<StoryViewParam>> get() = _storiesMap
    fun getStoriesWithLocation() = viewModelScope.launch {
        storiesRepositoryContract.getStoriesWithLocation().collectLatest {
            when (it) {
                is States.Loading -> {

                }
                is States.Success -> {
                    it.data?.let { data ->
                        _storiesMap.value = data
                    }
                }
                is States.Error -> {

                }
            }
        }
    }
}