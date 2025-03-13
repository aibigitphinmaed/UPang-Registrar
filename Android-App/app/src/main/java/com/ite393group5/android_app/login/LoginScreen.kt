package com.ite393group5.android_app.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ite393group5.android_app.R
import com.ite393group5.android_app.common.MediumTitleText
import com.ite393group5.android_app.common.TitleText
import com.ite393group5.android_app.login.state.LoginUiEvent
import timber.log.Timber

@Composable
fun LoginScreen(
    onNavigationToAuthenticatedRoutes: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel()
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .imePadding()
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    // Heading Jetpack Compose
                    MediumTitleText(
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .fillMaxWidth(),
                        text = "Upang Registrar",
                        textAlign = TextAlign.Center
                    )

                    // Login Logo
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Login Logo",
                        modifier = Modifier
                            .size(300.dp)
                            .padding(bottom = 16.dp)
                    )

                    // Transparent Card for Login Inputs
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .border(
                                width = 1.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        LoginInputs(
                            loginState = loginState,
                            onEmailChange = { inputString ->
                                loginViewModel.onUiEvent(
                                    LoginUiEvent.OnEmailChanged(inputString)
                                )
                            },
                            onPasswordChange = { inputString ->
                                loginViewModel.onUiEvent(
                                    LoginUiEvent.OnPasswordChanged(inputString)
                                )
                            },
                            onSubmit = {
                                loginViewModel.onUiEvent(
                                    LoginUiEvent.Submit
                                )
                            },
                            onVisibilityToggle = {
                                loginViewModel.onUiEvent(
                                    LoginUiEvent.OnVisibilityToggle("visibility")
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
