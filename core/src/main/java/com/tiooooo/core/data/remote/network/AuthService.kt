package com.tiooooo.core.data.remote.network

import com.tiooooo.core.data.remote.req.ReqModel
import com.tiooooo.core.data.remote.response.LoginResultResponse
import com.tiooooo.core.ui.base.BaseApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("register")
    suspend fun register(@Body registerReq: ReqModel.Register): BaseApiResponse

    @POST("login")
    suspend fun login(@Body loginReq: ReqModel.Login): LoginResultResponse
}