package com.tiooooo.core.data.remote.network

import com.tiooooo.core.data.remote.response.ListStoriesResponse
import com.tiooooo.core.ui.base.BaseApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface StoriesService {
    @GET("stories")
    suspend fun getStories(): ListStoriesResponse

    @Multipart
    @POST("stories")
    suspend fun createStories(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
    ): BaseApiResponse
}