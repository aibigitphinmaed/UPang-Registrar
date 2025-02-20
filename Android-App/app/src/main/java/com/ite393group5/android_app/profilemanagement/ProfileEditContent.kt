package com.ite393group5.android_app.profilemanagement

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ite393group5.android_app.models.LocationInfo
import com.ite393group5.android_app.models.PersonalInfo
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun ProfileEditContent(
    personalInfo: PersonalInfo,
    locationInfo: LocationInfo,
    profileScreenViewModel: ProfileScreenViewModel
) {
    val profileState by profileScreenViewModel.flowProfileState.collectAsState()

    val file = profileState.profileImageLocation?.let { File(it) }

    val profileBitmap = BitmapFactory.decodeFile(file?.absolutePath ?: "")

    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val pickMedia = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            Timber.tag("PhotoPicker").d("Selected URI: $uri")
            selectedImageUri = uri  // Store the selected image
            val fileFound = saveImageToInternalStorage(context, uri) // Save image
            profileScreenViewModel.updateProfileImageLocation(fileFound.absolutePath)
        } else {
            Timber.tag("PhotoPicker").d("No media selected")
        }
    }

    //region request permission

    val requestPermissions =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            if (results.all { it.value }) {

                Timber.tag("ProfileEditContent").e("Granted")
            } else {
                Timber.tag("ProfileEditContent").e("Denied")
            }
        }


    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            requestPermissions.launch(
                arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VISUAL_USER_SELECTED)
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES))
        } else {
            requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
        }
    }



//endregion


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (profileBitmap != null) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    bitmap = profileBitmap.asImageBitmap(),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(180.dp)
                        .clip(MaterialTheme.shapes.extraLarge),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                ) {
                    Text("Change Image")
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(MaterialTheme.shapes.large),
                    tint = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                ) {
                    Text("Change Image")
                }
            }

        }
        Spacer(Modifier.height(16.dp))

        //region Personal Info Fields
        OutlinedTextField(
            value = personalInfo.firstName ?: "",
            onValueChange = { profileScreenViewModel.updateFirstName(it) },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(2.dp))

        OutlinedTextField(
            value = personalInfo.middleName ?: "",
            onValueChange = { profileScreenViewModel.updateMiddleName(it) },
            label = { Text("Middle Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(2.dp))

        OutlinedTextField(
            value = personalInfo.lastName ?: "",
            onValueChange = { profileScreenViewModel.updateLastName(it) },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(2.dp))

        OutlinedTextField(
            value = personalInfo.extensionName ?: "",
            onValueChange = { profileScreenViewModel.updateExtensionName(it) },
            label = { Text("Extension Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(2.dp))

        var selectedGender by remember { mutableStateOf("Male") }


        GenderDropdown(selectedGender = selectedGender, onGenderSelected = { newGender ->
            selectedGender = newGender
            profileScreenViewModel.updateGender(newGender)
        })
        Spacer(modifier = Modifier.height(2.dp))

        OutlinedTextField(
            value = personalInfo.citizenship ?: "",
            onValueChange = { profileScreenViewModel.updateCitizenship(it) },
            label = { Text("Citizenship") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(2.dp))

        OutlinedTextField(
            value = personalInfo.religion ?: "",
            onValueChange = { profileScreenViewModel.updateReligion(it) },
            label = { Text("Religion") },
            modifier = Modifier.fillMaxWidth()
        )

        var selectedCivilStatus by remember { mutableStateOf("Single") }
        Spacer(modifier = Modifier.height(2.dp))

        CivilStatusDropdown(selectedStatus = selectedCivilStatus, onStatusSelected = { newStatus ->
            selectedCivilStatus = newStatus
            profileScreenViewModel.updateCivilStatus(newStatus)
        })
        Spacer(modifier = Modifier.height(2.dp))

        OutlinedTextField(
            value = personalInfo.number ?: "",
            onValueChange = { profileScreenViewModel.updatePhoneNumber(it) },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(2.dp))

        var showDatePicker by remember { mutableStateOf(false) }

        Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Birth Date: ${personalInfo.birthDate ?: "Not Set"}",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { showDatePicker = true }) {
                Text("Select Birth Date")
            }
        }

        if (showDatePicker){
            DatePickerModal(
                onDateSelected = { localDate ->
                    profileScreenViewModel.updateBirthDate(localDate)
                    showDatePicker = false
                },
                onDismiss = {
                    showDatePicker = false
                }
            )
        }


        Spacer(modifier = Modifier.height(2.dp))


        OutlinedTextField(
            value = personalInfo.fatherName ?: "",
            onValueChange = { profileScreenViewModel.updateFatherName(it) },
            label = { Text("Father's Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(2.dp))

        OutlinedTextField(
            value = personalInfo.motherName ?: "",
            onValueChange = { profileScreenViewModel.updateMotherName(it) },
            label = { Text("Mother's Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(2.dp))

        OutlinedTextField(
            value = personalInfo.spouseName ?: "",
            onValueChange = { profileScreenViewModel.updateSpouseName(it) },
            label = { Text("Spouse's Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(2.dp))

        OutlinedTextField(
            value = personalInfo.contactPersonNumber ?: "",
            onValueChange = { profileScreenViewModel.updateContactPersonNumber(it) },
            label = { Text("Contact Person's Number") },
            modifier = Modifier.fillMaxWidth()
        )


//endregion

//region address inputfields
        Spacer(Modifier.height(16.dp))
        HorizontalDivider(color = Color.Gray, thickness = 1.dp)
        Spacer(Modifier.height(16.dp))

        // Location Info Fields
        OutlinedTextField(
            value = locationInfo.houseNumber ?: "",
            onValueChange = { profileScreenViewModel.updateHouseNumber(it) },
            label = { Text("House Number") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(2.dp))

        OutlinedTextField(
            value = locationInfo.street ?: "",
            onValueChange = { profileScreenViewModel.updateStreet(it) },
            label = { Text("Street") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(2.dp))

        OutlinedTextField(
            value = locationInfo.zone ?: "",
            onValueChange = { profileScreenViewModel.updateZone(it) },
            label = { Text("Zone") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(2.dp))

        OutlinedTextField(
            value = locationInfo.barangay ?: "",
            onValueChange = { profileScreenViewModel.updateBarangay(it) },
            label = { Text("Barangay") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(2.dp))

        OutlinedTextField(
            value = locationInfo.cityMunicipality ?: "",
            onValueChange = { profileScreenViewModel.updateCity(it) },
            label = { Text("City/Municipality") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(2.dp))

        OutlinedTextField(
            value = locationInfo.province ?: "",
            onValueChange = { profileScreenViewModel.updateProvince(it) },
            label = { Text("Province") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(2.dp))

        OutlinedTextField(
            value = locationInfo.country ?: "",
            onValueChange = { profileScreenViewModel.updateCountry(it) },
            label = { Text("Country") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(2.dp))

        OutlinedTextField(
            value = locationInfo.postalCode ?: "",
            onValueChange = { profileScreenViewModel.updatePostalCode(it) },
            label = { Text("Postal Code") },
            modifier = Modifier.fillMaxWidth()
        )
        //endregion

        Spacer(Modifier.height(24.dp))
    }
}

fun saveImageToInternalStorage(context: Context, uri: Uri): File {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    val file = File(context.filesDir, "new_profile_image.png")
    inputStream?.use { input ->
        FileOutputStream(file).use { output ->
            input.copyTo(output)
        }
    }
    return file
}

@Composable
private fun GenderDropdown(selectedGender: String, onGenderSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("Male", "Female", "Prefer Not To Say")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .clickable { expanded = true }
            .padding(16.dp)
    ) {
        Text(text = selectedGender.ifEmpty { "Select Gender" }, color = Color.Black)

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            genderOptions.forEach { gender ->
                DropdownMenuItem(
                    text = { Text(text = gender) },
                    onClick = {
                        onGenderSelected(gender)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun CivilStatusDropdown(selectedStatus: String, onStatusSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val statusOptions = listOf("Single", "Married", "Widowed","Prefer Not To Say")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .clickable { expanded = true }
            .padding(16.dp)
    ) {
        Text(text = selectedStatus.ifEmpty { "Select Civil Status" }, color = Color.Black)

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            statusOptions.forEach { gender ->
                DropdownMenuItem(
                    text = { Text(text = gender) },
                    onClick = {
                        onStatusSelected(gender)
                        expanded = false
                    }
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (LocalDate?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val selectedDateMillis = datePickerState.selectedDateMillis
                val localDate = selectedDateMillis?.let {
                    Instant.ofEpochMilli(it)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                }
                onDateSelected(localDate)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
