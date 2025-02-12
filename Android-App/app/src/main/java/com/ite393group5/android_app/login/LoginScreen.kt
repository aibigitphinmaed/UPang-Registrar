package com.ite393group5.android_app.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ite393group5.android_app.R
import com.ite393group5.android_app.common.TitleText
import com.ite393group5.android_app.login.state.LoginUiEvent
import timber.log.Timber

@Composable
fun LoginScreen(
    onNavigationToAuthenticatedRoutes: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel<LoginViewModel>()
) {

    val loginState by loginViewModel.loginState.collectAsStateWithLifecycle()

    if (loginState.isLoggedIn) {
        LaunchedEffect(Unit) {
            Timber.tag("LoginScreen").e("Logged in")
            onNavigationToAuthenticatedRoutes.invoke()
        }
    } else {

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
                    .verticalScroll(rememberScrollState()).fillMaxSize(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Heading Jetpack Compose
                MediumTitleText(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth(),
                    text = "Upang Registrar",
                    textAlign = TextAlign.Center
                )

                // Login Logo
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Login Logo",
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                )

                // Heading Login
                TitleText(
                    text = "Welcome student!",
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .align(alignment = Alignment.CenterHorizontally),

                    )

                LoginInputs(
                    loginState = loginState,
                    onEmailChange = { inputString ->
                        loginViewModel.onUiEvent(
                            loginUiEvent = LoginUiEvent.OnEmailChanged(
                                inputString
                            )
                        )
                    },
                    onPasswordChange = { inputString ->
                        loginViewModel.onUiEvent(
                            loginUiEvent = LoginUiEvent.OnPasswordChanged(
                                inputString
                            )
                        )
                    },
                    onSubmit = {
                        loginViewModel.onUiEvent(
                            loginUiEvent = LoginUiEvent.Submit
                        )
                    }
                )

            }
        }

    }


}

@Composable
fun MediumTitleText(modifier: Modifier, text: String, textAlign: TextAlign) {

}
