package com.ite393group5.android_app.loadingscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ite393group5.android_app.common.CustomIndicators
import com.ite393group5.android_app.theme.CustomTheme

@Composable
fun LoadingScreen(
    navigateToDashboard: () -> Unit,
) {
    val loadingViewModel: LoadingViewModel = hiltViewModel<LoadingViewModel>()
    val loadingState by loadingViewModel.flowLoadingState.collectAsState()

    if (loadingState.waitingForAddressData || loadingState.waitingForProfileData) {
        Surface (modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars),
            color = Color.White,
            contentColor = Color.White,
        ){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CustomIndicators()
            }
        }
    } else {
        navigateToDashboard.invoke()
    }

}

@Preview
@Composable
fun PreviewLoadingScreen() {
    CustomTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column {
                LoadingScreen(navigateToDashboard = {})
            }
        }
    }

}

