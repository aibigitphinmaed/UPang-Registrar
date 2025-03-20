package com.ite393group5.android_app.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


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