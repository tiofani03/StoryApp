package com.tiooooo.core.contract

import androidx.lifecycle.LiveData
import com.tiooooo.core.model.UserModel
import com.tiooooo.core.model.LoginViewParam
import com.tiooooo.core.utils.States
import kotlinx.coroutines.flow.Flow

interface AuthRepositoryContract {
    suspend fun register(email: String, password: String, name: String): Flow<States<String>>
    suspend fun login(email: String, password: String): Flow<States<LoginViewParam>>
    fun isLogin(): Boolean
    fun getToken(): LiveData<String>
    fun logout()
    fun getUser(): LiveData<UserModel>
}