package com.ite393group5.android_app.utilities


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.ite393group5.android_app.R
import com.ite393group5.android_app.profilemanagement.ProfileScreenViewModel
import com.ite393group5.android_app.theme.CustomTheme
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun DashboardBottomBar(modifier: Modifier, onNavigateToProfileScreen: () -> Unit) {

    BottomAppBar(
        actions = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                IconButton(onClick = { /* do something */ }) {
                    Icon(Icons.Filled.Notifications, contentDescription = stringResource(R.string.notification))
                }
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        Icons.Filled.ContentPaste,
                        contentDescription = stringResource(R.string.document),
                    )
                }
                IconButton(onClick = { onNavigateToProfileScreen.invoke() }) {
                    Icon(
                        Icons.Filled.AccountBox,
                        contentDescription = stringResource(R.string.profile_management_content_description),
                    )
                }

            }
        })

}

@Composable
fun ProfileBottomBar(modifier: Modifier, profileScreenViewModel: ProfileScreenViewModel ) {
    BottomAppBar(
        actions = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ){

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    IconButton(onClick = { profileScreenViewModel.startEditMode() }) {
                        Icon(Icons.Filled.Edit, contentDescription = stringResource(R.string.notification))
                    }
                    Text("Edit")
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    IconButton(onClick = { profileScreenViewModel.changePasswordMode() }) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = stringResource(R.string.notification))
                    }
                    Text("Change Password")
                }

            }
        })
}

@Composable
fun ProfileConfirmBottomBar(confirmEdit: ()->Unit, cancelEdit: ()->Unit){
    BottomAppBar(
        actions = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ){

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    IconButton(onClick = { confirmEdit.invoke() }) {
                        Icon(Icons.Filled.Check, contentDescription = stringResource(R.string.notification))
                    }
                    Text("Confirm Edit")
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    IconButton(onClick = { cancelEdit.invoke() }) {
                        Icon(Icons.Filled.Cancel, contentDescription = stringResource(R.string.notification))
                    }
                    Text("Cancel Edit")
                }

            }
        })
}





@Preview
@Composable
fun PreviewBottomBars(){
    CustomTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            DashboardBottomBar(
                modifier = Modifier,
                onNavigateToProfileScreen = {}
            )
        }

    }
}