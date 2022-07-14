package com.tiooooo.core.contract

import androidx.paging.PagingData
import com.tiooooo.core.model.StoryViewParam
import com.tiooooo.core.utils.States
import kotlinx.coroutines.flow.Flow
import java.io.File

interface StoriesRepositoryContract {
    fun getStories(): Flow<PagingData<StoryViewParam>>
    fun getStoriesWithLocation(): Flow<States<List<StoryViewParam>>>
    suspend fun createStories(
        description: String,
        image: File,
        latitude: Double? = null,
        longitude: Double?= null
    ): Flow<States<Boolean>>
}