package com.ite393group5.android_app.login.state

import com.ite393group5.android_app.R
import com.ite393group5.android_app.login.ErrorState


val emailEmptyErrorState = ErrorState(
    hasError = true,
    errorMessageStringResource = R.string.login_error_msg_empty_email
)

val passwordEmptyErrorState = ErrorState(
    hasError = true,
    errorMessageStringResource = R.string.password_empty_error_msg
)


