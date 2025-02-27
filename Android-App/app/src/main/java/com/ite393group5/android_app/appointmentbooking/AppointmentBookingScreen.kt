package com.ite393group5.android_app.appointmentbooking

import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ite393group5.android_app.R
import com.ite393group5.android_app.common.CustomFAB
import com.ite393group5.android_app.utilities.CustomAppTopbar
import com.ite393group5.android_app.utilities.CustomConfirmBottomBar
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppointmentBookingScreen(
    modifier: Modifier,
    openDrawer: () -> Unit,
    appointmentBookingViewModel: AppointmentBookingViewModel = hiltViewModel()
) {
    val uiState by appointmentBookingViewModel.stateAppointmentView.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        appointmentBookingViewModel.toastMessage.collectLatest { message ->
            val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }

    Scaffold(
        topBar = {
            when {

                uiState.isCreatingAppointment ->
                    CustomAppTopbar(
                        title = "Create Appointment", openDrawer = openDrawer,
                        modifier = Modifier
                    )

                uiState.isModifyingAppointment ->
                    CustomAppTopbar(
                        title = "Modify Appointment", openDrawer = openDrawer,
                        modifier = Modifier
                    )

                uiState.hasAppointment -> {
                    CustomAppTopbar(
                        title = "Your current appointment", openDrawer = openDrawer,
                        modifier = Modifier
                    )
                }
                !uiState.hasAppointment -> {
                    CustomAppTopbar(
                        title = "No Appointment Made", openDrawer = openDrawer,
                        modifier = Modifier
                    )
                }

            }

        },
        bottomBar = {
            when {
                uiState.isCreatingAppointment && !uiState.firstConfirmation ->
                    CustomConfirmBottomBar(
                        {appointmentBookingViewModel.firstConfirmationRequest()},
                        { appointmentBookingViewModel.cancelOnFirstConfirmationRequest()}
                    )
                uiState.isCreatingAppointment && uiState.firstConfirmation ->
                    CustomConfirmBottomBar(
                        { appointmentBookingViewModel.confirmAppointmentRequest()},
                        { appointmentBookingViewModel.goBackToCreateAppointment() }
                    )

                uiState.hasAppointment -> {
                    Text("Your current appointment")
                }
                !uiState.hasAppointment -> {}

            }
        },
        floatingActionButton = {
            when {
                uiState.isCreatingAppointment -> {}
                uiState.isModifyingAppointment -> {}
                uiState.hasAppointment -> {}
                !uiState.hasAppointment -> {
                    CustomFAB("create appointment", onClick = {
                        appointmentBookingViewModel.createAppointmentRequest()
                    })
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        when {
            uiState.isCreatingAppointment -> CreateAnAppointmentScreen(paddingValues)
            uiState.isModifyingAppointment -> ModifyingSelectedAppointment(paddingValues)

            uiState.hasAppointment -> {}
            !uiState.hasAppointment -> {
                NoAppointmentMade(paddingValues)
            }
        }
    }
}




@Composable
fun ModifyingSelectedAppointment(paddingValues: PaddingValues) {

}

@Composable
fun NoAppointmentMade(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center // Centers the content inside the Box
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // Centers items horizontally
            verticalArrangement = Arrangement.Center // Ensures proper vertical spacing
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_warning_amber_24),
                contentDescription = "No Appointment",
                modifier = Modifier.size(250.dp)
            )
            Spacer(modifier = Modifier.height(16.dp)) // Adds spacing between Image and Text
            Text(
                text = "No Appointment Made",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
