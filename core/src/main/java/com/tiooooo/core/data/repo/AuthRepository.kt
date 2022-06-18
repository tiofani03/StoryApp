package com.tiooooo.core.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.tiooooo.core.contract.AuthRepositoryContract
import com.tiooooo.core.model.UserModel
import com.tiooooo.core.data.local.UserPreference
import com.tiooooo.core.data.remote.network.AuthService
import com.tiooooo.core.data.remote.req.ReqModel
import com.tiooooo.core.model.LoginViewParam
import com.tiooooo.core.utils.States
import com.tiooooo.core.utils.network.NetworkUtils.getErrorMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class AuthRepository(
    private val service: AuthService,
    private val userPreference: UserPreference
) : AuthRepositoryContract {
    override suspend fun register(
        email: String,
        password: String,
        name: String
    ): Flow<States<String>> = flow {
        try {
            emit(States.Loading())
            val req = ReqModel.Register(email, password, name)
            val response = service.register(req)
            response.message?.let {
                emit(States.Success(it))
            }
        } catch (e: Exception) {
            emit(States.Error(e.getErrorMessage()))
            Timber.e(e.getErrorMessage())
        }
    }.flowOn(Dispatchers.IO)


    override suspend fun login(
        email: String,
        password: String
    ): Flow<States<LoginViewParam>> = flow {
        try {
            emit(States.Loading())
            val req = ReqModel.Login(email, password)
            val response = service.login(req)
            if (!response.loginResult.token.isNullOrEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val user = UserModel(
                        name = response.loginResult.name.orEmpty(),
                        accessToken = response.loginResult.token.orEmpty(),
                        email = email,
                        isLogin = true
                    )
                    userPreference.login(user)
                }
                emit(States.Success(response.loginResult.toClean()))
            }
        } catch (e: Exception) {
            emit(States.Error(e.getErrorMessage()))
            Timber.e(e.getErrorMessage())
        }
    }.flowOn(Dispatchers.IO)

    override fun isLogin(): Boolean {
        return runBlocking { userPreference.isLogin().first() }
    }

    override fun getUser(): LiveData<UserModel> {
        return userPreference.getUser().asLiveData()
    }

    override fun getToken(): LiveData<String> {
        return userPreference.getToken().asLiveData()
    }

    override fun logout(){
        CoroutineScope(Dispatchers.IO).launch {
            userPreference.logout()
        }
    }

}
