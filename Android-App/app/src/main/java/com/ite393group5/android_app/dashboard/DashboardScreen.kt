package com.ite393group5.android_app.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ite393group5.android_app.theme.CustomTheme
import com.ite393group5.android_app.utilities.CustomAppTopbar
import com.ite393group5.android_app.utilities.DashboardBottomBar
import timber.log.Timber


@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    openDrawer: () -> Unit,
    dashboardViewModel: DashboardViewModel = hiltViewModel<DashboardViewModel>(),
    onNavigateToProfileScreen: () -> Unit
) {

    val uiState by dashboardViewModel.dashboardStateView.collectAsState()

    Timber.tag("DashboardScreen").d("DashboardScreen: ${uiState.personalInfo?.firstName}")
    Scaffold(
        topBar = {
            CustomAppTopbar("Hello, ${uiState.personalInfo?.firstName}", openDrawer, modifier = modifier)
        },
        bottomBar = { DashboardBottomBar(modifier, onNavigateToProfileScreen) },
        floatingActionButton = {},
        floatingActionButtonPosition = FabPosition.End,
        ) { innerPadding ->
        Column( modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(innerPadding)
            .padding(horizontal = 16.dp)) {
           //announcement section
            AnnouncementSection()
            //request section
            RequestSection(Modifier.weight(1f))
            Spacer(modifier = Modifier.weight(1f))
            //feedback section
            FeedbackSection()
        }

    }


}

@Composable
fun AnnouncementSection() {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Announcement", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("We are closed on Weekends and Holidays!")
            Spacer(modifier = Modifier.height(8.dp))
            Text("02/22/2025", fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun RequestSection(modifier: Modifier) {
    Column {
        Text("Upcoming", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Document type: Copy of Grade")
                Spacer(modifier = Modifier.height(4.dp))
                Text("Requested Date: 02/11/2026", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Approved", color = Color.Green)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Pending", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text("No Pending Request", color = Color.Gray)
    }
}

@Composable
fun FeedbackSection() {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Feedback", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text("Send us your feedback to help improve our service!", textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {},
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text("Send Feedback", color = Color.White)
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