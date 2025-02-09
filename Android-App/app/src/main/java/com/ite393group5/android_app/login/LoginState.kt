package com.ite393group5.android_app.login

import androidx.annotation.StringRes
import com.ite393group5.android_app.R
import kotlinx.serialization.Serializable

@Serializable
data class LoginState(
    val email:String = "",
    val password:String = "",
    val errorState: LoginErrorState = LoginErrorState(),
    val isLoading:Boolean = false,
    val isLoggedIn:Boolean = false
)

@Serializable
data class LoginErrorState(
    val emailErrorState:ErrorState = ErrorState(),
    val passwordErrorState:ErrorState = ErrorState()
)

@Serializable
data class ErrorState(
    val hasError:Boolean = false,
    @StringRes val errorMessageStringResource: Int = R.string.empty_string
)