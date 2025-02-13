package com.ite393group5.android_app.profilemanagement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ite393group5.android_app.common.EditableProfileField

@Composable
fun ProfileEditContent(paddingValues: PaddingValues, profileScreenViewModel: ProfileScreenViewModel) {

    var personalInfo by remember { mutableStateOf(profileScreenViewModel.flowProfileState.value.personalInfo) }
    var locationInfo by remember { mutableStateOf(profileScreenViewModel.flowProfileState.value.locationInfo) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,


    ) {
        // Profile Image
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profile Picture",
            modifier = Modifier.size(150.dp),
            tint = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Editable Personal Info
        EditableProfileField("First Name", personalInfo?.firstName) {
            personalInfo = personalInfo?.copy(firstName = it)
        }
        EditableProfileField("Last Name", personalInfo?.lastName) {
            personalInfo = personalInfo?.copy(lastName = it)
        }
        EditableProfileField("Email", personalInfo?.email, KeyboardType.Email) {
            personalInfo = personalInfo?.copy(email = it)
        }
        EditableProfileField(
            "Phone Number",
            personalInfo?.number,
            KeyboardType.Phone
        ) { personalInfo = personalInfo?.copy(number = it) }

        Spacer(modifier = Modifier.height(16.dp))

        // Editable Location Info
        EditableProfileField("Street", locationInfo?.street) {
            locationInfo = locationInfo?.copy(street = it)
        }
        EditableProfileField("City", locationInfo?.cityMunicipality) {
            locationInfo = locationInfo?.copy(cityMunicipality = it)
        }
        EditableProfileField("Province", locationInfo?.province) {
            locationInfo = locationInfo?.copy(province = it)
        }
        EditableProfileField("Country", locationInfo?.country) {
            locationInfo = locationInfo?.copy(country = it)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save Button
        Button(
            onClick = {
                profileScreenViewModel.showConfirmWindow(personalInfo,locationInfo)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }
    }
}