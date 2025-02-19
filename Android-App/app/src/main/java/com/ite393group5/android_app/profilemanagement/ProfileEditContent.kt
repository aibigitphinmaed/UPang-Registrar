package com.ite393group5.android_app.profilemanagement

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.ite393group5.android_app.models.LocationInfo
import com.ite393group5.android_app.models.PersonalInfo

@Composable
fun ProfileEditContent(
    personalInfo: PersonalInfo,
    locationInfo: LocationInfo,
    profileScreenViewModel: ProfileScreenViewModel
) {
    val profileBitmapFlow = profileScreenViewModel.profileBitmapFlow
    val profileBitmap = profileBitmapFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (profileBitmap.value != null) {
            profileBitmap.value?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(180.dp)
                        .clip(MaterialTheme.shapes.extraLarge),
                    contentScale = ContentScale.Crop
                )
            }
        } else {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(MaterialTheme.shapes.large),
                tint = Color.Gray
            )
        }
        Spacer(Modifier.height(16.dp))

        // Personal Info Fields
        OutlinedTextField(
            value = personalInfo.firstName ?: "",
            onValueChange = { profileScreenViewModel.updateFirstName(it) },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = personalInfo.middleName ?: "",
            onValueChange = { profileScreenViewModel.updateMiddleName(it) },
            label = { Text("Middle Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = personalInfo.lastName ?: "",
            onValueChange = { profileScreenViewModel.updateLastName(it) },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = personalInfo.number ?: "",
            onValueChange = { profileScreenViewModel.updatePhoneNumber(it) },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // Location Info Fields
        OutlinedTextField(
            value = locationInfo.houseNumber ?: "",
            onValueChange = { profileScreenViewModel.updateHouseNumber(it) },
            label = { Text("House Number") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = locationInfo.street ?: "",
            onValueChange = { profileScreenViewModel.updateStreet(it) },
            label = { Text("Street") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = locationInfo.zone ?: "",
            onValueChange = { profileScreenViewModel.updateZone(it) },
            label = { Text("Zone") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = locationInfo.barangay ?: "",
            onValueChange = { profileScreenViewModel.updateBarangay(it) },
            label = { Text("Barangay") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = locationInfo.cityMunicipality ?: "",
            onValueChange = { profileScreenViewModel.updateCity(it) },
            label = { Text("City/Municipality") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = locationInfo.province ?: "",
            onValueChange = { profileScreenViewModel.updateProvince(it) },
            label = { Text("Province") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = locationInfo.country ?: "",
            onValueChange = { profileScreenViewModel.updateCountry(it) },
            label = { Text("Country") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = locationInfo.postalCode ?: "",
            onValueChange = { profileScreenViewModel.updatePostalCode(it) },
            label = { Text("Postal Code") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))


    }
}
