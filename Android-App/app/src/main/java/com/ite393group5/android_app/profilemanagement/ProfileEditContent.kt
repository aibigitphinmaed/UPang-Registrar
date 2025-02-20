package com.ite393group5.android_app.profilemanagement

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.ImageButton
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
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

