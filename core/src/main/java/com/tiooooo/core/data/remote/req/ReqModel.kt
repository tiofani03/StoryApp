package com.tiooooo.core.data.remote.req

import com.google.gson.annotations.SerializedName

object ReqModel {
    data class Register(
        @SerializedName("email") val email: String? = null,
        @SerializedName("password") val password: String? = null,
        @SerializedName("name") val name: String? = null,
    )

    data class Login(
        @SerializedName("email") val email: String? = null,
        @SerializedName("password") val password: String? = null,
    )
}