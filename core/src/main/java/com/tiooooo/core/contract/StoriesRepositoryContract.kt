package com.tiooooo.core.contract

import com.tiooooo.core.model.StoriesViewParam
import com.tiooooo.core.utils.States
import kotlinx.coroutines.flow.Flow
import java.io.File

interface StoriesRepositoryContract {
    suspend fun getStories(): Flow<States<ArrayList<StoriesViewParam>>>
    suspend fun createStories(
        description: String,
        image: File
    ): Flow<States<Boolean>>
}