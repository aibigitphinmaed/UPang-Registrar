package com.ite393group5.android_app.profilemanagement

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ite393group5.android_app.common.InfoRow
import com.ite393group5.android_app.models.LocationInfo
import com.ite393group5.android_app.models.PersonalInfo
import com.ite393group5.android_app.profilemanagement.state.ProfileScreenState
import timber.log.Timber
import java.io.File


@Composable
fun ProfileContent(
    personalInfo: PersonalInfo,
    locationInfo: LocationInfo,
    profileState: ProfileScreenState,
    ) {


    Timber.tag("ProfileContent").e(profileState.profileImageLocation)
    val file = profileState.profileImageLocation?.let { File(it) }
    if (file != null) {
        if (!file.exists()) {
            Timber.tag("ProfileContent").e("File does not exist at path: ${profileState.profileImageLocation}")
        }
    }else{
        Timber.tag("ProfileContent").e("File is null at path: ${profileState.profileImageLocation}")
    }
    val profileBitmap: Bitmap? = BitmapFactory.decodeFile(file?.absolutePath ?: "")



    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (profileBitmap != null) {
            Image(
                bitmap = profileBitmap.asImageBitmap(),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(180.dp)
                    .clip(MaterialTheme.shapes.extraLarge),
                contentScale = ContentScale.Crop
            )
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
        Text("${personalInfo.firstName ?: "N/A"} ${personalInfo.middleName ?: ""} ${personalInfo.lastName ?: ""}", fontSize = 20.sp, style = MaterialTheme.typography.headlineSmall)
        HorizontalDivider(Modifier.padding(vertical = 16.dp))

        Card(Modifier.fillMaxWidth(), elevation = CardDefaults.elevatedCardElevation(4.dp)) {
            Column(Modifier.padding(16.dp)) {
                Text("Personal Information", fontSize = 18.sp, fontWeight = MaterialTheme.typography.headlineSmall.fontWeight)
                InfoRow("Extension Name", personalInfo.extensionName ?: "N/A")
                InfoRow("Gender", personalInfo.gender ?: "N/A")
                InfoRow("Citizenship", personalInfo.citizenship ?: "N/A")
                InfoRow("Religion", personalInfo.religion ?: "N/A")
                InfoRow("Civil Status", personalInfo.civilStatus ?: "N/A")
                InfoRow("Email", personalInfo.email ?: "N/A")
                InfoRow("Phone", personalInfo.number ?: "N/A")
                InfoRow("Birth Date", personalInfo.birthDate?.toString() ?: "N/A")
                InfoRow("Father's Name", personalInfo.fatherName ?: "N/A")
                InfoRow("Mother's Name", personalInfo.motherName ?: "N/A")
                InfoRow("Spouse Name", personalInfo.spouseName ?: "N/A")
                InfoRow("Contact Person Number", personalInfo.contactPersonNumber ?: "N/A")
            }
        }

        Spacer(Modifier.height(16.dp))
        Card(Modifier.fillMaxWidth(), elevation = CardDefaults.elevatedCardElevation(4.dp)) {
            Column(Modifier.padding(16.dp)) {
                Text("Address Information", fontSize = 18.sp, fontWeight = MaterialTheme.typography.headlineSmall.fontWeight)
                InfoRow("House Number", locationInfo.houseNumber ?: "N/A")
                InfoRow("Street", locationInfo.street ?: "N/A")
                InfoRow("Zone", locationInfo.zone ?: "N/A")
                InfoRow("Barangay", locationInfo.barangay ?: "N/A")
                InfoRow("City", locationInfo.cityMunicipality ?: "N/A")
                InfoRow("Province", locationInfo.province ?: "N/A")
                InfoRow("Country", locationInfo.country ?: "N/A")
                InfoRow("Postal Code", locationInfo.postalCode ?: "N/A")
            }
        }
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