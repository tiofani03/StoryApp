package com.tiooooo.core.data.remote.response

import com.google.gson.annotations.SerializedName
import com.tiooooo.core.model.LoginViewParam
import com.tiooooo.core.ui.base.BaseApiResponse

data class LoginResultResponse(
    @SerializedName("loginResult") val loginResult: LoginResultRes
) : BaseApiResponse()

data class LoginResultRes(
    @SerializedName("name") val name: String? = null,
    @SerializedName("userId") val userId: String? = null,
    @SerializedName("token") val token: String? = null
) {
    fun toClean(): LoginViewParam {
        return LoginViewParam(
            name = name.orEmpty(),
            userId = userId.orEmpty(),
            token = token.orEmpty()
        )
    }
}
