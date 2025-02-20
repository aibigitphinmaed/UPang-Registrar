package com.ite393group5.android_app.profilemanagement

import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ite393group5.android_app.utilities.CustomAppTopbar
import com.ite393group5.android_app.utilities.ProfileBottomBar
import com.ite393group5.android_app.utilities.ProfileConfirmBottomBar
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    openDrawer: () -> Unit,
    profileScreenViewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val profileScreenState by profileScreenViewModel.flowProfileState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        profileScreenViewModel.toastMessage.collectLatest { message ->
            val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }


    Scaffold(
        topBar = {
            CustomAppTopbar(
                title = "Profile", openDrawer = openDrawer,
                modifier = Modifier
            )
        },
        bottomBar = {
            when {
                profileScreenState.editMode -> ProfileConfirmBottomBar(
                    confirmEdit = { profileScreenViewModel.showConfirmWindow() },
                    cancelEdit = { profileScreenViewModel.cancelEditMode() }
                )

                profileScreenState.showConfirmWindow -> ProfileConfirmBottomBar({ profileScreenViewModel.completeEditing() }, { profileScreenViewModel.cancelConfirmation() })


                profileScreenState.changePasswordMode && !profileScreenState.confirmChangePasswordMode -> ProfileConfirmBottomBar(
                    confirmEdit = { profileScreenViewModel.confirmChangePassword()
                    },
                    cancelEdit = { profileScreenViewModel.cancelChangePassword() }
                )

                profileScreenState.changePasswordMode && profileScreenState.confirmChangePasswordMode -> ProfileConfirmBottomBar(
                    confirmEdit = { profileScreenViewModel.completeChangePassword() },
                    cancelEdit = { profileScreenViewModel.cancelConfirmChangePassword() }
                )


                else -> ProfileBottomBar(modifier, profileScreenViewModel)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when {
                profileScreenState.editMode -> EditModeProfile(profileScreenViewModel)

                profileScreenState.showConfirmWindow -> {
                    ViewModeProfile(profileScreenViewModel)
                    Toast.makeText(
                        context,
                        "Click Confirm Again to Complete Profile Update",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                profileScreenState.changePasswordMode -> ProfileChangePassword(profileScreenViewModel)


                else -> ViewModeProfile(profileScreenViewModel)
            }
        }
    }
}



@Composable
fun ViewModeProfile(profileScreenViewModel: ProfileScreenViewModel) {
    val profileState by profileScreenViewModel.flowProfileState.collectAsState()

    profileState.personalInfo?.let { personalInfo ->
        profileState.locationInfo?.let { locationInfo ->
            ProfileContent(personalInfo, locationInfo, profileState)
        }
    } ?: Text(
        "Loading Profile...", modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}

@Composable
fun EditModeProfile(profileScreenViewModel: ProfileScreenViewModel) {
    val profileState by profileScreenViewModel.flowProfileState.collectAsState()

    profileState.personalInfo?.let { personalInfo ->
        profileState.locationInfo?.let { locationInfo ->
            ProfileEditContent(
                personalInfo, locationInfo,
                profileScreenViewModel = profileScreenViewModel
            )
        }
    } ?: Text(
        "Loading Profile...", modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
}

