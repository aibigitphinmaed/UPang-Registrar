package com.ite393group5.android_app.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val calendar = Calendar.getInstance()

    // Get the next valid date (skipping weekends)
    fun getNextValidDate(startingOffset: Int): Long {
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.DAY_OF_MONTH, startingOffset)

        while (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return calendar.timeInMillis
    }

    val minDateMillis = getNextValidDate(2) // At least 3 days ahead
    val recommendedDateMillis = getNextValidDate(3) // Preferably 5 days ahead

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = recommendedDateMillis)

    var isInvalidDate by remember { mutableStateOf(false) }

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val selectedDateMillis = datePickerState.selectedDateMillis
                    if (selectedDateMillis != null) {
                        calendar.timeInMillis = selectedDateMillis
                        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

                        if (selectedDateMillis >= minDateMillis && dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                            isInvalidDate = false
                            onDateSelected(selectedDateMillis)
                            onDismiss()
                        } else {
                            isInvalidDate = true
                        }
                    }
                },
                enabled = datePickerState.selectedDateMillis != null
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        Column {
            DatePicker(state = datePickerState)

            // Warning message for invalid selection
            if (isInvalidDate) {
                Text(
                    text = "⚠️ Please select a valid weekday at least 2-3 days ahead.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
