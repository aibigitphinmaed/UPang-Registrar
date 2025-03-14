package com.ite393group5.android_app.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun PasswordTextField(
    label: String,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onVisibilityToggle: () -> Unit,
    isError: Boolean = false,
    errorText: String = "",
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text(label) },
        isError = isError,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        enabled = enabled,
        trailingIcon = {
            IconButton(onClick = onVisibilityToggle) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                )
            }
        }
    )
    if (isError) {
        Text(errorText, color = MaterialTheme.colorScheme.error)
    }
}

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    errorText: String = "",
    imeAction: ImeAction = ImeAction.Done
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = {
            Text(text = label)
        },
        maxLines = 1,
        isError = isError,
        supportingText = {
            if (isError) {
                ErrorTextInputField(text = errorText)
            }
        },
        trailingIcon = {
            IconButton(onClick = {
                isPasswordVisible = !isPasswordVisible
            }) { }
        },

        )
}


@Composable
fun EmailTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    errorText: String = "",
    imeAction: ImeAction = ImeAction.Next
) {


    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = label)
        },
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = imeAction
        ),
        isError = isError,
        supportingText = {
            if (isError) {
                ErrorTextInputField(text = errorText)
            }
        }
    )

}

@Composable
fun EditableProfileField(
    label: String,
    value: String?,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelSmall)
        OutlinedTextField(
            value = value ?: "",
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTextFieldComposables(){

    Column {
        EmailTextField(
            value = "",
            onValueChange = {},
            label = "Email",
            isError = true,)

        EditableProfileField(
            label = "ProfileField",
            value = "Profile Value",
            keyboardType = KeyboardType.Text,
            onValueChange = {},
        )
    }

}