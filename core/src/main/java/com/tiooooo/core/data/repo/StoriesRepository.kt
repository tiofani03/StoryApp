package com.tiooooo.core.data.repo

import com.tiooooo.core.contract.StoriesRepositoryContract
import com.tiooooo.core.data.remote.network.StoriesService
import com.tiooooo.core.model.StoriesViewParam
import com.tiooooo.core.utils.States
import com.tiooooo.core.utils.network.NetworkUtils.getErrorMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class StoriesRepository(private val service: StoriesService) : StoriesRepositoryContract {
    override suspend fun getStories(): Flow<States<ArrayList<StoriesViewParam>>> = flow {
        try {
            emit(States.Loading())
            val response = service.getStories()
            val listStories = ArrayList<StoriesViewParam>()
            listStories.addAll(response.listStory?.map {
                it.toClean()
            } ?: arrayListOf())
            emit(States.Success(listStories))
        } catch (e: Exception) {
            emit(States.Error(e.getErrorMessage()))
            Timber.e(e)
        }
    }.flowOn(Dispatchers.IO)

}