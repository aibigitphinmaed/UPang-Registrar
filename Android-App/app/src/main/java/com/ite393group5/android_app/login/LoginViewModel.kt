package com.ite393group5.android_app.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ite393group5.android_app.login.state.LoginUiEvent
import com.ite393group5.android_app.login.state.emailEmptyErrorState
import com.ite393group5.android_app.login.state.passwordEmptyErrorState
import com.ite393group5.android_app.models.LoginRequest
import com.ite393group5.android_app.services.local.LocalServiceImpl
import com.ite393group5.android_app.services.remote.RemoteServiceImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val localServiceImpl: LocalServiceImpl,
    private val remoteServiceImpl: RemoteServiceImpl
) : ViewModel(){



    private val _loginState = MutableStateFlow(LoginState())  // Use private mutable state
    val loginState: StateFlow<LoginState> = _loginState  // Expose as immutable State

    init {
        viewModelScope.launch(Dispatchers.IO){
            localServiceImpl.collectFromDataStore()
            val sessionIsOn = localServiceImpl.checkSession()
            Timber.tag("LoginViewModel").e(sessionIsOn.toString())
            _loginState.value = loginState.value.copy(
                isLoggedIn = sessionIsOn
            )
        }
    }


    fun onUiEvent(loginUiEvent: LoginUiEvent){
        when(loginUiEvent){
            is LoginUiEvent.OnEmailChanged -> {
                _loginState.value = _loginState.value.copy(
                    email = loginUiEvent.inputValue,
                    errorState = _loginState.value.errorState.copy(emailErrorState = if(loginUiEvent.inputValue.trim().isNotEmpty())
                        ErrorState() else emailEmptyErrorState
                    )
                )
            }

            is LoginUiEvent.OnPasswordChanged -> {
                _loginState.value = _loginState.value.copy(
                    password = loginUiEvent.inputValue,
                    errorState = _loginState.value.errorState.copy(
                        passwordErrorState = if (loginUiEvent.inputValue.trim().isNotEmpty())
                            ErrorState()
                        else
                            passwordEmptyErrorState
                    )
                )
            }

            is LoginUiEvent.Submit -> {
                if(validateInputs()){
                    //submit login
                    viewModelScope.launch {
                        val loginRequest = LoginRequest(
                            username = _loginState.value.email,
                            password = _loginState.value.password
                        )
                        _loginState.value = _loginState.value.copy(
                            isLoggedIn = remoteServiceImpl.login(loginRequest)
                        )
                    }

                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        val emailString = _loginState.value.email.trim()
        val passwordString = _loginState.value.password

        return when {
            emailString.isEmpty() -> {
                _loginState.value = _loginState.value.copy(
                    errorState = _loginState.value.errorState.copy(
                        emailErrorState = emailEmptyErrorState
                    )
                )
                false
            }

            passwordString.isEmpty() -> {
                _loginState.value = _loginState.value.copy(
                    errorState = _loginState.value.errorState.copy(
                        passwordErrorState = passwordEmptyErrorState
                    )
                )
                false
            }

            //no empty errors
            else -> {
                //set all error state to false
                _loginState.value = _loginState.value.copy(
                    errorState = LoginErrorState()
                )
                true
            }
        }
    }


}