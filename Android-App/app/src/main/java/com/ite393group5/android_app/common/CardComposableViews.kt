package com.ite393group5.android_app.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ite393group5.android_app.theme.CustomTheme
import com.ite393group5.android_app.models.Appointment
@Composable
fun DashboardCard(title: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth(1f)
            .height(128.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(icon, contentDescription = title, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(title, style = MaterialTheme.typography.bodyMedium)
                Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun AppointmentItem(dateTime: String, status: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.CalendarToday, contentDescription = "Appointment", modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(dateTime, style = MaterialTheme.typography.bodyMedium)
                Text(status, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}

@Composable
fun AppointmentItem(appointment: Appointment) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Type: ${appointment.appointmentType}", style = MaterialTheme.typography.bodyLarge)
            appointment.documentType?.let {
                Text(text = "Document: $it", style = MaterialTheme.typography.bodyMedium)
            }
            Text(text = "Status: ${appointment.status}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Date: ${appointment.requestedDate ?: "N/A"}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@Composable
fun AnnouncementCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Info, contentDescription = "Announcement", modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(message, style = MaterialTheme.typography.bodyMedium)
        }
    }
}



@Preview
@Composable
fun PreviewCards(){
    CustomTheme {
        Column {
            DashboardCard("Appointments Today", "32", Icons.Default.Event)
            DashboardCard("Now Serving", "#23", Icons.Default.ConfirmationNumber)
            DashboardCard("Avg. Wait Time", "15 min", Icons.Default.Timer)
            Text("Upcoming Appointments", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                items(5) { index ->
                    AppointmentItem("Feb 15, 2025 - 10:30 AM", "Pending")
                }
            }
            Text("Registrar Announcements", style = MaterialTheme.typography.titleMedium)
            AnnouncementCard("System maintenance on Feb 20, expect delays.")
        }
    }
}
