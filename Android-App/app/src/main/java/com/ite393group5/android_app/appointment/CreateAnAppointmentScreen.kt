package com.ite393group5.android_app.appointment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ite393group5.android_app.common.CustomDropdownMenu
import com.ite393group5.android_app.common.DatePickerModal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CreateAnAppointmentScreen(
    paddingValues: PaddingValues,
    onDocumentTypeChange: (String) -> Unit,
    onAppointmentTypeChange: (String) -> Unit,
    onRequestedDateChange: (String) -> Unit
) {
    var selectedAppointmentType by remember { mutableStateOf("") }
    var selectedDocumentType by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()

    // Function to find the next valid weekday (skips weekends)
    fun getNextValidDate(startingOffset: Int): String {
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.DAY_OF_MONTH, startingOffset)

        while (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    }

    val minDateStr = getNextValidDate(3) // Minimum: 3 days ahead
    val preferredDateStr = getNextValidDate(5) // Recommended: 5 days ahead

    if (selectedDate.isEmpty()) {
        selectedDate = preferredDateStr // Set default date
    }

    val registrarAppointmentTypes = listOf(
        "Transcript Request", "Diploma & Certificate Issuance",
        "Enrollment Verification", "Course Add/Drop & Withdrawal",
        "Student Records Update", "Academic Standing & Graduation Eligibility",
        "Leave of Absence & Reinstatement", "Cross-Enrollment & Special Permission Requests",
        "Late Registration & Overload Requests", "Document Authentication & Certification"
    )

    val documentTypesMap = mapOf(
        "Transcript Request" to listOf("Official Transcript", "Unofficial Transcript"),
        "Diploma & Certificate Issuance" to listOf("Diploma", "Certificate of Graduation"),
        "Enrollment Verification" to listOf("Enrollment Certificate", "Letter of Verification"),
        "Course Add/Drop & Withdrawal" to listOf("Add/Drop Form", "Withdrawal Request"),
        "Student Records Update" to listOf("Name Change Request", "Address Update"),
        "Academic Standing & Graduation Eligibility" to listOf("Graduation Audit", "Academic Standing Letter"),
        "Leave of Absence & Reinstatement" to listOf("Leave Request", "Reinstatement Application"),
        "Cross-Enrollment & Special Permission Requests" to listOf("Cross-Enrollment Form", "Permission Letter"),
        "Late Registration & Overload Requests" to listOf("Late Registration Form", "Overload Request"),
        "Document Authentication & Certification" to listOf("Certified True Copy", "Authenticated Documents")
    )

    val requiredDocumentsMap = mapOf(
        "Official Transcript" to listOf("Valid Student ID", "Payment Receipt"),
        "Unofficial Transcript" to listOf("Valid Student ID"),
        "Diploma" to listOf("Graduation Clearance", "Valid Student ID"),
        "Certificate of Graduation" to listOf("Graduation Clearance"),
        "Enrollment Certificate" to listOf("Valid Student ID", "Proof of Enrollment"),
        "Letter of Verification" to listOf("Valid Student ID", "Request Form"),
        "Add/Drop Form" to listOf("Completed Add/Drop Form", "Adviser Approval"),
        "Withdrawal Request" to listOf("Withdrawal Request Form", "Dean's Approval"),
        "Name Change Request" to listOf("Birth Certificate", "Government ID"),
        "Address Update" to listOf("Proof of Residence", "Valid Student ID"),
        "Graduation Audit" to listOf("Academic Records", "Graduation Checklist"),
        "Academic Standing Letter" to listOf("Request Form", "Dean's Approval"),
        "Leave Request" to listOf("Leave of Absence Form", "Parental Consent (if minor)"),
        "Reinstatement Application" to listOf("Reinstatement Form", "Medical Clearance (if applicable)"),
        "Cross-Enrollment Form" to listOf("Cross-Enrollment Approval", "Valid Student ID"),
        "Permission Letter" to listOf("Request Form", "Department Approval"),
        "Late Registration Form" to listOf("Late Registration Approval", "Valid Student ID"),
        "Overload Request" to listOf("Academic Standing Proof", "Adviser Approval"),
        "Certified True Copy" to listOf("Original Document", "Valid Student ID"),
        "Authenticated Documents" to listOf("Original Documents", "Authentication Request Form")
    )

    val documentTypes by remember(selectedAppointmentType) {
        derivedStateOf { documentTypesMap[selectedAppointmentType] ?: emptyList() }
    }

    val requiredDocuments by remember(selectedDocumentType) {
        derivedStateOf { requiredDocumentsMap[selectedDocumentType] ?: emptyList() }
    }


    onAppointmentTypeChange(selectedAppointmentType)
    onDocumentTypeChange(selectedDocumentType)
    onRequestedDateChange(selectedDate)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Create an Appointment", style = MaterialTheme.typography.headlineMedium)

            // Appointment Type Dropdown
            CustomDropdownMenu(
                label = "Select Appointment Type",
                options = registrarAppointmentTypes,
                selectedOption = selectedAppointmentType,
                onOptionSelected = {
                    selectedAppointmentType = it
                    selectedDocumentType = documentTypes.firstOrNull() ?: ""
                    onAppointmentTypeChange(it)
                }
            )

            // Document Type Dropdown
            if (selectedAppointmentType.isNotEmpty()) {
                CustomDropdownMenu(
                    label = "Select Document Type",
                    options = documentTypes,
                    selectedOption = selectedDocumentType,
                    onOptionSelected = {
                        selectedDocumentType = it
                        onDocumentTypeChange(it)
                    }
                )
            }

            // Required Documents Display
            if (selectedDocumentType.isNotEmpty() && requiredDocuments.isNotEmpty()) {
                Text("Required Documents:", style = MaterialTheme.typography.bodyLarge)
                requiredDocuments.forEach { document ->
                    Text("- $document", style = MaterialTheme.typography.bodyMedium)
                }
            }


            // Hint Message for Date Selection
            Text(
                text = "Appointments must be at least 2-3 days ahead. We recommend 5 days ahead.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )

            // Requested Date Picker
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Requesting Date: \n ${selectedDate.ifEmpty { "Not Set" }}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { showDatePicker = true }, modifier = Modifier.weight(2f)) {
                    Text("Open Calendar", textAlign = TextAlign.Center)
                }
            }

            // Warning Message (if date is invalid)
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Show Date Picker Modal when triggered
            if (showDatePicker) {
                DatePickerModal(
                    onDateSelected = {
                        val selectedDateMillis = it
                        val selectedDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDateMillis)
                        onRequestedDateChange(selectedDateStr)
                        // Validate the selected date
                        if (selectedDateStr >= minDateStr) {
                            selectedDate = selectedDateStr
                            errorMessage = "" // Clear any previous error
                        } else {
                            errorMessage = "Invalid date! Please choose a weekday at least 3 days ahead."
                        }
                        showDatePicker = false
                    },
                    onDismiss = { showDatePicker = false }
                )
            }
        }
    }
}
