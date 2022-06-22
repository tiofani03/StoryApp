package com.tiooooo.core.contract

import androidx.paging.PagingData
import com.tiooooo.core.model.StoryViewParam
import com.tiooooo.core.utils.States
import kotlinx.coroutines.flow.Flow
import java.io.File

interface StoriesRepositoryContract {
    fun getStories(): Flow<PagingData<StoryViewParam>>
    suspend fun createStories(
        description: String,
        image: File
    ): Flow<States<Boolean>>
}