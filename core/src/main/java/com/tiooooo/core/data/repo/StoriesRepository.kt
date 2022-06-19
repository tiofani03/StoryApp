package com.tiooooo.core.data.repo

import com.tiooooo.core.contract.StoriesRepositoryContract
import com.tiooooo.core.data.remote.network.StoriesService
import com.tiooooo.core.model.StoryViewParam
import com.tiooooo.core.utils.States
import com.tiooooo.core.utils.network.NetworkUtils.getErrorMessage
import com.tiooooo.core.utils.network.NetworkUtils.toPart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File

class StoriesRepository(private val service: StoriesService) : StoriesRepositoryContract {
    override suspend fun getStories(): Flow<States<ArrayList<StoryViewParam>>> = flow {
        try {
            emit(States.Loading())
            val response = service.getStories()
            val listStories = ArrayList<StoryViewParam>()
            listStories.addAll(response.listStory?.map {
                it.toClean()
            } ?: arrayListOf())
            emit(States.Success(listStories))
        } catch (e: Exception) {
            emit(States.Error(e.getErrorMessage()))
            Timber.e(e)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun createStories(description: String, image: File): Flow<States<Boolean>> =
        flow {
            try {
                emit(States.Loading())
                val desReq = description.toRequestBody()
                val photo = image.toPart("photo")
                val response = service.createStories(desReq, photo)
                emit(States.Success(response.isError ?: false))
            } catch (e: Exception) {
                emit(States.Error(e.getErrorMessage()))
                Timber.e(e)
            }
        }.flowOn(Dispatchers.IO)

}