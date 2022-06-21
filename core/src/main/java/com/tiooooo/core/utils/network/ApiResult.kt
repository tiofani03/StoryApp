package com.tiooooo.core.utils.network

import com.tiooooo.core.ui.base.BaseApiResponse

sealed class ApiResult<out T : Any> {

    class Success<out T : Any>(val data: T) : ApiResult<T>()

    class Error(val exception: Throwable) : ApiResult<Nothing>() {
        constructor(message: String?, messageTitle: String?, data: Any?) : this(Throwable(message)) {
            this.message = message ?: BaseApiResponse.GENERAL_ERROR
            this.messageTitle = messageTitle
            this.data = data
        }

        private var message: String = ""
        private var messageTitle: String? = ""
        var data: Any? = null
            private set
    }

}
