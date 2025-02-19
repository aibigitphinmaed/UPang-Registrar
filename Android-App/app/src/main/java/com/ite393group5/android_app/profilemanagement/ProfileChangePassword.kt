package com.ite393group5.android_app.profilemanagement


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ite393group5.android_app.common.PasswordTextField

@Composable
fun ProfileChangePassword(profileScreenViewModel: ProfileScreenViewModel) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var newPasswordError by remember { mutableStateOf(false) }
    var confirmNewPasswordError by remember { mutableStateOf(false) }

    var currentPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmNewPasswordVisible by remember { mutableStateOf(false) }

    val profileState by profileScreenViewModel.flowProfileState.collectAsState()

    LaunchedEffect(currentPassword,newPassword, confirmNewPassword) {
        newPasswordError = newPassword.isEmpty()
        confirmNewPasswordError = confirmNewPassword.isEmpty() || newPassword != confirmNewPassword


            profileScreenViewModel.updateVarPassword(currentPassword, newPassword, confirmNewPassword)

    }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PasswordTextField(
            label = "Current Password",
            password = currentPassword,
            onPasswordChange = { currentPassword = it },
            passwordVisible = currentPasswordVisible,
            onVisibilityToggle = { currentPasswordVisible = !currentPasswordVisible },
            enabled = !profileState.confirmChangePasswordMode
        )

        Spacer(modifier = Modifier.height(8.dp))

        PasswordTextField(
            label = "New Password",
            password = newPassword,
            onPasswordChange = { newPassword = it },
            passwordVisible = newPasswordVisible,
            onVisibilityToggle = { newPasswordVisible = !newPasswordVisible },
            isError = newPasswordError,
            errorText = "New password cannot be empty",
            enabled = !profileState.confirmChangePasswordMode
        )

        Spacer(modifier = Modifier.height(8.dp))

        PasswordTextField(
            label = "Confirm New Password",
            password = confirmNewPassword,
            onPasswordChange = { confirmNewPassword = it },
            passwordVisible = confirmNewPasswordVisible,
            onVisibilityToggle = { confirmNewPasswordVisible = !confirmNewPasswordVisible },
            isError = confirmNewPasswordError,
            errorText = "Passwords do not match",
            enabled = !profileState.confirmChangePasswordMode
        )
    }
}


