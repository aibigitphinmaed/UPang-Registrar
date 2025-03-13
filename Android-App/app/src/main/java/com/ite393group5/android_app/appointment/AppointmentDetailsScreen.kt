package com.ite393group5.android_app.appointment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ite393group5.android_app.models.Appointment


@Composable
fun AppointmentDetailsScreen(appointment: Appointment, paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f) // Adjust width to avoid stretching too much
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppointmentDetailItem(label = "Type", value = appointment.appointmentType)
                AppointmentDetailItem(label = "Document", value = appointment.documentType ?: "N/A")
                AppointmentDetailItem(label = "Reason", value = appointment.reason ?: "N/A")
                AppointmentDetailItem(label = "Requested Date", value = appointment.requestedDate?.toString() ?: "N/A")
                AppointmentDetailItem(label = "Scheduled Date", value = appointment.scheduledDate?.toString() ?: "N/A")
                AppointmentDetailItem(label = "Status", value = appointment.status)
                AppointmentDetailItem(label = "Remarks", value = appointment.remarks ?: "N/A")
                AppointmentDetailItem(label = "Cancellation Reason", value = appointment.cancellationReason ?: "N/A")
            }
        }
    }
}


@Composable
fun AppointmentDetailItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), thickness = 0.5.dp)
    }
}
