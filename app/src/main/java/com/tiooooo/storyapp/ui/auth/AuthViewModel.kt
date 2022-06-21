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

    private val _loginState = MutableLiveData<Boolean>()
    private val _login = MutableLiveData<LoginViewParam>()
    private val _loginError = MutableLiveData<String>()

    val loginState: LiveData<Boolean> get() = _loginState
    val login get(): LiveData<LoginViewParam> = _login
    val loginError get(): LiveData<String> = _loginError
    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password).collectLatest {
                when (it) {
                    is States.Loading -> _loginState.value = true
                    is States.Success -> {
                        _loginState.value = false
                        it.data?.let { it1 -> _login.value = it1 }
                    }
                    is States.Error -> {
                        _loginState.value = false
                        _loginError.value = it.message.orEmpty()
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

    private val _registerState = MutableLiveData<Boolean>()
    private val _register = MutableLiveData<String>()
    private val _registerError = MutableLiveData<String>()

    val registerState: LiveData<Boolean> get() = _registerState
    val register: LiveData<String> get() = _register
    val registerError: LiveData<String> get() = _registerError
    fun register(email: String, name: String, password: String) = viewModelScope.launch {
        authRepository.register(
            email,
            password,
            name
        ).collectLatest {
            when (it) {
                is States.Loading -> _registerState.value = true
                is States.Success -> {
                    _registerState.value = false
                    it.data?.let { data -> _register.value = data }
                }
                is States.Error -> {
                    _registerState.value = false
                    _registerError.value = it.message.orEmpty()
                }
            }
        }
    }

}
