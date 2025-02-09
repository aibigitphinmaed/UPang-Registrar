package com.ite393group5.android_app.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.ite393group5.android_app.login.LoginState
import com.ite393group5.android_app.login.state.LoginUiEvent
import com.ite393group5.android_app.login.state.emailEmptyErrorState
import com.ite393group5.android_app.login.state.passwordEmptyErrorState

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel(){

    val loginState = mutableStateOf(LoginState())

    init {
        //viewModelScope.launch(Dispatchers.IO)
    }

    fun onUiEvent(loginUiEvent: LoginUiEvent){
        when(loginUiEvent){
            is LoginUiEvent.OnEmailChanged -> {
                loginState.value = loginState.value.copy(
                    email = loginUiEvent.inputValue,
                    errorState = loginState.value.errorState.copy(emailErrorState = if(loginUiEvent.inputValue.trim().isNotEmpty())
                        ErrorState() else emailEmptyErrorState
                    )
                )
            }

            is LoginUiEvent.OnPasswordChanged -> {
                loginState.value = loginState.value.copy(
                    password = loginUiEvent.inputValue,
                    errorState = loginState.value.errorState.copy(
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
                    loginState.value = loginState.value.copy(
                        isLoggedIn = true
                    )
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        val emailString = loginState.value.email.trim()
        val passwordString = loginState.value.password

        return when {
            emailString.isEmpty() -> {
                loginState.value = loginState.value.copy(
                    errorState = loginState.value.errorState.copy(
                        emailErrorState = emailEmptyErrorState
                    )
                )
                false
            }

            passwordString.isEmpty() -> {
                loginState.value = loginState.value.copy(
                    errorState = loginState.value.errorState.copy(
                        passwordErrorState = passwordEmptyErrorState
                    )
                )
                false
            }

            //no empty errors
            else -> {
                //set all error state to false
                loginState.value = loginState.value.copy(
                    errorState = LoginErrorState()
                )
                true
            }
        }
    }


}