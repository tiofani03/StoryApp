package com.tiooooo.storyapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiooooo.core.contract.AuthRepositoryContract
import com.tiooooo.core.model.LoginViewParam
import com.tiooooo.core.utils.States
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepositoryContract,
) : ViewModel() {

    val loginState = MutableLiveData<Boolean>()
    val login = MutableLiveData<LoginViewParam>()
    val loginError = MutableLiveData<String>()
    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password).collectLatest {
                when (it) {
                    is States.Loading -> loginState.value = true
                    is States.Success -> {
                        loginState.value = false
                        it.data?.let { it1 -> login.value = it1 }
                    }
                    is States.Error -> {
                        loginState.value = false
                        loginError.value = it.message.orEmpty()
                    }
                }
            }
        }
    }

    val textEmailLogin = MutableLiveData<Boolean>()
    val textPasswordLogin = MutableLiveData<Boolean>()
    val btnVerificationLogin = MutableLiveData<Boolean>()

    fun validateLoginButton(): Boolean =
        (textEmailLogin.value == true && textPasswordLogin.value == true).also {
            btnVerificationLogin.value = it
        }


    val textEmailRegister = MutableLiveData<Boolean>()
    val textPasswordRegister = MutableLiveData<Boolean>()
    val textNameRegister = MutableLiveData<Boolean>()
    val btnVerificationRegister = MutableLiveData<Boolean>()

    fun validateRegisterButton(): Boolean =
        (textEmailRegister.value == true && textPasswordRegister.value == true && textNameRegister.value == true).also {
            btnVerificationRegister.value = it
        }


    fun getToken(): LiveData<String> = authRepository.getToken()

    val registerState = MutableLiveData<Boolean>()
    val register = MutableLiveData<String>()
    val registerError = MutableLiveData<String>()
    fun register(email: String, name: String, password: String) = viewModelScope.launch {
        authRepository.register(
            email,
            password,
            name
        ).collectLatest {
            when (it) {
                is States.Loading -> registerState.value = true
                is States.Success -> {
                    registerState.value = false
                    it.data?.let { data -> register.value = data }
                }
                is States.Error -> {
                    registerState.value = false
                    registerError.value = it.message.orEmpty()
                }
            }
        }
    }

}
