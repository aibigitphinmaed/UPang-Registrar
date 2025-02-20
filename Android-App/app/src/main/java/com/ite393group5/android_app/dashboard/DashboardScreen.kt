package com.ite393group5.android_app.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ite393group5.android_app.common.AnnouncementCard
import com.ite393group5.android_app.common.DashboardCard
import com.ite393group5.android_app.dashboard.screens.QueueTracker
import com.ite393group5.android_app.theme.CustomTheme
import com.ite393group5.android_app.utilities.CustomAppTopbar
import com.ite393group5.android_app.utilities.DashboardBottomBar


@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    openDrawer: () -> Unit,
    dashboardViewModel: DashboardViewModel = hiltViewModel<DashboardViewModel>(),
    onNavigateToProfileScreen: () -> Unit
) {

    Scaffold(
        topBar = {
            CustomAppTopbar("Dashboard", openDrawer, modifier = modifier)
        },
        bottomBar = { DashboardBottomBar(modifier, onNavigateToProfileScreen) },
        floatingActionButton = {},
        floatingActionButtonPosition = FabPosition.End,
        ) { innerPadding ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .windowInsetsPadding(WindowInsets.systemBars),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 8.dp) ,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){

                Text(
                    "Welcome to the Registrar's Office",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth()

                )

                // Key Metrics (Queue Status, Appointments, Wait Time)

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(listOf(
                        "Now Serving" to "#23",
                        "Avg. Wait Time" to "15 min"
                    )) { (title, value) ->
                        DashboardCard(title, value, Icons.Default.Event, Modifier.width(160.dp))
                    }
                }


                // Live Queue Tracker
                QueueTracker(
                    currentQueueNumber = 23,
                    estimatedWaitTime = "15 min",
                    onJoinQueue = { /* Handle Join Queue */ },
                    onCancelQueue = { /* Handle Cancel Queue */ }
                )


                // Announcements Section
                Text("Registrar Announcements", style = MaterialTheme.typography.titleMedium)
                AnnouncementCard("System maintenance on Feb 20, expect delays.")

            }
        }


    }


}


@Preview(showBackground = true)
@Composable
fun PreviewDashboard() {
    CustomTheme {
        DashboardScreen(
            modifier = Modifier,
            openDrawer = {},
            onNavigateToProfileScreen = {},
        )
    }
}