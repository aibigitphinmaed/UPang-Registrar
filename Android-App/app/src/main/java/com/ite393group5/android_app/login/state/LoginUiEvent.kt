package com.ite393group5.android_app.login.state

sealed class LoginUiEvent {
    data class OnEmailChanged(val inputValue:String) : LoginUiEvent()
    data class OnPasswordChanged(val inputValue:String) : LoginUiEvent()
    data object Submit : LoginUiEvent()
    data class OnVisibilityToggle(val inputValue:String): LoginUiEvent()
}