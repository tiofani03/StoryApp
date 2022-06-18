package com.tiooooo.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoriesViewParam(
    val id: String = "",
    val name: String = "",
    val photoUrl: String = "",
    val createdAt: String = "",
    val description: String = "",
    val lon: Double = 0.0,
    val lat: Double = 0.0
) : Parcelable