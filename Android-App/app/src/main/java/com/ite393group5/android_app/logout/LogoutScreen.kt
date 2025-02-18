package com.ite393group5.android_app.logout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ite393group5.android_app.common.CustomIndicators
import com.ite393group5.android_app.common.NormalButton

@Composable
fun LogoutScreen(
    onNavigationToUnauthenticatedRoutes: () -> Unit,
    logoutViewModel: LogoutViewModel = hiltViewModel<LogoutViewModel>()
) {

    val confirmState by logoutViewModel.confirmState.collectAsStateWithLifecycle() // readonly lang ito
    val clearingState by logoutViewModel.clearingState.collectAsStateWithLifecycle() //readonly lang ito
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars),
        color = Color.White,
        contentColor = Color.White,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (confirmState) {
                if (clearingState) {
                    CustomIndicators()
                } else {
                    onNavigationToUnauthenticatedRoutes.invoke()
                }

            } else {
                NormalButton(
                    modifier = Modifier, text = "Click Again for Sign out confirmation", onClick = {
                        logoutViewModel.startClearingDataStore()
                    })
            }
        }
    }
}