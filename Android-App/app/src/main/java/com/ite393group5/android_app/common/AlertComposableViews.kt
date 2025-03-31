package com.ite393group5.android_app.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun WarningCancellation(
    onDismissRequest: (Boolean) -> Unit,
    onConfirmation: (Boolean) -> Unit,
    dialogTitle: String,
    dialogText: String,
    ){

    AlertDialog(
        icon = {
            Icon(imageVector = Icons.Default.Warning, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest(false)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation(true)
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest(false)
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun WarningDialog(
    dialogTitle: String,
    dialogText: String,
){

    AlertDialog(
        icon = {
            Icon(imageVector = Icons.Default.WarningAmber, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
        },
        confirmButton = {
            TextButton(
                onClick = {

                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {

                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun WarningDialog(
    dialogTitle: String,
    dialogText: String,
    onDismissRequest:() -> Unit
){

    AlertDialog(
        icon = {
            Icon(imageVector = Icons.Default.WarningAmber, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest.invoke()
        },
        confirmButton = {

        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest.invoke()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun NoInternetScreen(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.WifiOff,
                contentDescription = "No Internet",
                tint = Color.Red, // Adjust color if needed
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No Internet Connection",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Please check your connection and try again.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview
@Composable
fun WarningCancellationPreview(){
    WarningCancellation(
        onDismissRequest = {},
        onConfirmation = {},
        dialogTitle = "title",
        dialogText = "text"
    )
}