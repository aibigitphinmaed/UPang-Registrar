package com.ite393group5.android_app.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ite393group5.android_app.theme.CustomTheme
import com.ite393group5.android_app.utilities.CustomAppTopbar


@Composable
fun DashboardScreen(
    modifier: Modifier,
    openDrawer: () -> Unit,

) {
    val dashboardViewModel: DashboardViewModel = hiltViewModel<DashboardViewModel>()

    CustomAppTopbar(
        title = "Dashboard",
        openDrawer = openDrawer,
        modifier = modifier,
    )


}


@Preview(showBackground = true)
@Composable
fun PreviewDashboard() {
    CustomTheme {
        DashboardScreen(
            modifier = Modifier,
            openDrawer = {},
        )
    }
}