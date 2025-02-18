package com.ite393group5.android_app.dashboard.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ite393group5.android_app.theme.CustomTheme

@Composable
fun QueueTracker(
    currentQueueNumber: Int,
    estimatedWaitTime: String,
    onJoinQueue: () -> Unit,
    onCancelQueue: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Now Serving: #$currentQueueNumber", style = MaterialTheme.typography.headlineSmall)
            Text("Estimated Wait Time: $estimatedWaitTime", color = Color.Gray)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(onClick = onJoinQueue) {
                    Text("Join Queue")
                }
                OutlinedButton(onClick = onCancelQueue) {
                    Text("Cancel")
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewQueueTracker(){
    CustomTheme {
        QueueTracker(
            currentQueueNumber = 23,
            estimatedWaitTime = "15 min",
            onJoinQueue = { /* Handle Join Queue */ },
            onCancelQueue = { /* Handle Cancel Queue */ }
        )
    }
}
