package com.ite393group5.android_app.profilemanagement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ite393group5.android_app.profilemanagement.state.ProfileScreenState
import com.ite393group5.android_app.utilities.CustomAppTopbar
import com.ite393group5.android_app.utilities.ProfileBottomBar
import com.ite393group5.android_app.utilities.ProfileConfirmBottomBar


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    openDrawer: () -> Unit,
    profileScreenViewModel: ProfileScreenViewModel = hiltViewModel<ProfileScreenViewModel>()
) {
    val profileScreenState by profileScreenViewModel.flowProfileState.collectAsState()

    if(!profileScreenState.editMode){
        ViewModeProfile(modifier,openDrawer,profileScreenViewModel)
    }
    if(profileScreenState.editMode){
        EditModeProfile(modifier,openDrawer,profileScreenViewModel)
    }
    if(profileScreenState.showConfirmWindow){
        ConfirmModeProfile(modifier,openDrawer,profileScreenViewModel)
    }

}


@Composable
fun EditModeProfile(
    modifier: Modifier,
    openDrawer: () -> Unit,
    profileScreenViewModel: ProfileScreenViewModel,
) {
    Scaffold(
        topBar = {
            CustomAppTopbar(
                title = "Edit Profile",
                openDrawer = openDrawer,
                modifier = modifier
            )
        },

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileEditContent(paddingValues,profileScreenViewModel)
        }
    }
}



@Composable
fun ViewModeProfile(
    modifier: Modifier,
    openDrawer: () -> Unit,
    profileScreenViewModel: ProfileScreenViewModel,
) {





    Scaffold(
        topBar = {
            CustomAppTopbar(
                title = "Profile Management",
                openDrawer = openDrawer,
                modifier = modifier
            )
        },
        bottomBar = {
            ProfileBottomBar(
                modifier = Modifier,
                profileScreenViewModel = profileScreenViewModel
            )
        }
    ) {
            paddingValues ->

        ProfileContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            profileScreenViewModel
        )


    }


}

@Composable
fun ConfirmModeProfile(
    modifier: Modifier,
    openDrawer: () -> Unit,
    profileScreenViewModel: ProfileScreenViewModel,
) {

    Scaffold(
        topBar = {
            CustomAppTopbar(
                title = "Profile Management",
                openDrawer = openDrawer,
                modifier = modifier
            )
        },
        bottomBar = {
            ProfileConfirmBottomBar(
                modifier = modifier,
                profileScreenViewModel = profileScreenViewModel
            )
        }
    ) {
            paddingValues ->

        ProfileContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            profileScreenViewModel
        )


    }


}