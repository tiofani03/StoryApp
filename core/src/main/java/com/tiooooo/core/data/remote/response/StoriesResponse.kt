package com.tiooooo.core.data.remote.response

import com.google.gson.annotations.SerializedName
import com.tiooooo.core.model.StoryViewParam
import com.tiooooo.core.ui.base.BaseApiResponse


data class ListStoriesResponse(
    @SerializedName("listStory") val listStory: List<StoriesResponse>? = null,
) : BaseApiResponse()

data class StoriesResponse(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("photoUrl") val photoUrl: String? = null,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("lon") val lon: Double? = null,
    @SerializedName("lat") val lat: Double? = null
) {
    fun toClean(): StoryViewParam = StoryViewParam(
        id.orEmpty(),
        name.orEmpty(),
        photoUrl.orEmpty(),
        createdAt.orEmpty(),
        description.orEmpty(),
        lon ?: 0.0,
        lat ?: 0.0
    )
}
