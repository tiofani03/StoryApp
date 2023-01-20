package com.tiooooo.core.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tiooooo.core.contract.StoriesRepositoryContract
import com.tiooooo.core.data.datasource.StoryPagingSource
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
    override fun getStories(): Flow<PagingData<StoryViewParam>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            pagingSourceFactory = {
                StoryPagingSource(service)
            }
        ).flow
    }

    override fun getStoriesWithLocation(): Flow<States<List<StoryViewParam>>> =
        flow {
            try {
                emit(States.Loading())
                val res = service.getStories(location = 1)
                emit(States.Success(res.listStory?.map { it.toClean() } ?: emptyList()))
            } catch (e: Exception) {
                emit(States.Error(e.getErrorMessage()))
                Timber.e(e.getErrorMessage())
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun createStories(
        description: String,
        image: File,
        latitude: Double?,
        longitude: Double?
    ): Flow<States<Boolean>> =
        flow {
            try {
                emit(States.Loading())
                val desReq = description.toRequestBody()
                val photo = image.toPart("photo")
                val lat = latitude.toString().toRequestBody()
                val lon = longitude.toString().toRequestBody()
                val response = service.createStories(desReq, photo, lat, lon)
                emit(States.Success(response.isError ?: false))
            } catch (e: Exception) {
                emit(States.Error(e.getErrorMessage()))
                Timber.e(e)
            }
        }.flowOn(Dispatchers.IO)

}
