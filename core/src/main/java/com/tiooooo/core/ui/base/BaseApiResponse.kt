package com.tiooooo.core.ui.base

import com.google.gson.annotations.SerializedName
import com.tiooooo.core.utils.network.ApiResult

open class BaseApiResponse {

    companion object {
        const val GENERAL_ERROR = "GENERAL_ERROR"
    }


    @SerializedName("error")
    var isError: Boolean? = false

    @SerializedName("message")
    var message: String? = null

    open fun <T : Any> getResult(data: T): ApiResult<T> {
        return if (isError == false) {
            ApiResult.Success(data)
        } else {
            ApiResult.Error(Throwable("error"))
        }
    }

}