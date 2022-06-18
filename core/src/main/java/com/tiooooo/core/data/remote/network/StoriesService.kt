package com.tiooooo.core.data.remote.network

import com.tiooooo.core.data.remote.response.ListStoriesResponse
import retrofit2.http.GET

interface StoriesService {
    @GET("stories")
    suspend fun getStories(): ListStoriesResponse
}