package com.tiooooo.core.contract

import com.tiooooo.core.model.StoriesViewParam
import com.tiooooo.core.utils.States
import kotlinx.coroutines.flow.Flow

interface StoriesRepositoryContract {
    suspend fun getStories(): Flow<States<ArrayList<StoriesViewParam>>>
}