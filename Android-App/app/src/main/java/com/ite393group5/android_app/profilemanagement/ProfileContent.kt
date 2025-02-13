package com.ite393group5.android_app.profilemanagement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ite393group5.android_app.common.InfoRow
import com.ite393group5.android_app.models.PersonalInfo
import java.time.format.DateTimeFormatter


@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    profileScreenViewModel: ProfileScreenViewModel,
    ) {
    val profileState = profileScreenViewModel.flowProfileState.collectAsState()

    val personalInfo = profileState.value.personalInfo
    val locationInfo = profileState.value.locationInfo

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(150.dp) // Adjust the size as needed
                .align(Alignment.CenterHorizontally)
                .scale(1.2f), // Optional scaling
            tint = Color.Gray // You can change the color
        )
        Text(
            text = "Personal Information",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp).align(Alignment.CenterHorizontally)
        )
        InfoRow(label = "Name", value = formatFullName(personalInfo))
        InfoRow(label = "Gender", value = personalInfo?.gender)
        InfoRow(label = "Email", value = personalInfo?.email)
        InfoRow(label = "Phone", value = personalInfo?.number)
        InfoRow(label = "Birth Date", value = personalInfo?.birthDate?.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")))
        InfoRow(label = "Citizenship", value = personalInfo?.citizenship)
        InfoRow(label = "Religion", value = personalInfo?.religion)
        InfoRow(label = "Civil Status", value = personalInfo?.civilStatus)
        InfoRow(label = "Father’s Name", value = personalInfo?.fatherName)
        InfoRow(label = "Mother’s Name", value = personalInfo?.motherName)
        InfoRow(label = "Spouse’s Name", value = personalInfo?.spouseName)
        InfoRow(label = "Emergency Contact", value = personalInfo?.contactPersonNumber)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Location Information",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        InfoRow(label = "House No.", value = locationInfo?.houseNumber)
        InfoRow(label = "Street", value = locationInfo?.street)
        InfoRow(label = "Zone", value = locationInfo?.zone)
        InfoRow(label = "Barangay", value = locationInfo?.barangay)
        InfoRow(label = "City/Municipality", value = locationInfo?.cityMunicipality)
        InfoRow(label = "Province", value = locationInfo?.province)
        InfoRow(label = "Country", value = locationInfo?.country)
        InfoRow(label = "Postal Code", value = locationInfo?.postalCode)
    }
}

fun formatFullName(personalInfo: PersonalInfo?): String {
    return listOfNotNull(
        personalInfo?.firstName,
        personalInfo?.middleName,
        personalInfo?.lastName,
        personalInfo?.extensionName
    ).joinToString(" ")
}