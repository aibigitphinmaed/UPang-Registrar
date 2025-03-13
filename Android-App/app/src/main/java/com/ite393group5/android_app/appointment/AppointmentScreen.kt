package com.ite393group5.android_app.appointment

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ite393group5.android_app.R
import com.ite393group5.android_app.common.CustomFAB
import com.ite393group5.android_app.utilities.CustomAppTopbar
import com.ite393group5.android_app.utilities.CustomConfirmBottomBar
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@Composable
fun AppointmentScreen(
    openDrawer: () -> Unit,
    appointmentViewModel: AppointmentViewModel = hiltViewModel(),
    modifier: Modifier
) {
    val uiState by appointmentViewModel.stateAppointmentView.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        appointmentViewModel.toastMessage.collectLatest { message ->
            val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }

    Scaffold(
        topBar = {
            Column {
                // Existing top bar logic
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

                    !uiState.hasInternet -> {
                        CustomAppTopbar(
                            title = "No Internet Connection", openDrawer = openDrawer,
                            modifier = Modifier
                        )
                    }
                }
            }
        },
        bottomBar = {
            when {
                uiState.isCreatingAppointment && !uiState.firstConfirmation ->
                    CustomConfirmBottomBar(
                        { appointmentViewModel.firstConfirmationRequest() },
                        { appointmentViewModel.cancelOnFirstConfirmationRequest() },
                        optionCheckName = "make an appointment",
                        optionCancelName = "cancel"
                    )

                uiState.isCreatingAppointment && uiState.firstConfirmation ->
                    CustomConfirmBottomBar(
                        {
                            appointmentViewModel.confirmAppointmentRequest()
                        },
                        { appointmentViewModel.goBackToCreateAppointment() },
                        optionCheckName = "confirm appointment",
                        optionCancelName = "cancel"
                    )

                uiState.hasAppointment && !uiState.isModifyingAppointment -> {
                    CustomConfirmBottomBar(
                        { appointmentViewModel.modifyCurrentAppointment() },
                        { appointmentViewModel.cancelCurrentAppointment() },
                        optionCheckName = "modify appointment",
                        optionCancelName = "cancel appointment"
                    )

                }
                uiState.hasAppointment && uiState.isModifyingAppointment -> {
                    CustomConfirmBottomBar(
                        { appointmentViewModel.finalizeModifyCurrentAppointment() },
                        { appointmentViewModel.cancelModifyCurrentAppointment() },
                        optionCheckName = "finalize modify appointment",
                        optionCancelName = "cancel" )
                }


                !uiState.hasAppointment -> {}

            }
        },
        floatingActionButton = {
            when {
                uiState.isCreatingAppointment -> {}
                uiState.isModifyingAppointment -> {}
                uiState.hasAppointment -> {

                }
                !uiState.hasAppointment -> {
                    CustomFAB("create appointment", onClick = {
                        appointmentViewModel.createAppointmentRequest()
                    })
                }

                !uiState.hasInternet -> {}
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        if (!uiState.hasInternet) {
            // Show No Internet UI
            NoInternetScreen(paddingValues)
        } else {
            // Normal UI logic
            when {
                uiState.isCreatingAppointment -> CreateAnAppointmentScreen(
                    paddingValues,
                    onDocumentTypeChange = {
                        appointmentViewModel.updateDocumentType(it)
                        Timber.tag("cAR").e(it)
                    },
                    onAppointmentTypeChange = {
                        appointmentViewModel.updateAppointmentType(it)
                        Timber.tag("cAR").e(it)
                    },
                    onRequestedDateChange = {
                        appointmentViewModel.updateRequestedDate(it)
                        Timber.tag("cAR").e(it)
                    }
                )
                uiState.isModifyingAppointment && uiState.newAppointmentState != null-> ModifyAppointmentScreen(
                    paddingValues = paddingValues,
                    onChangeDocumentType = {
                        appointmentViewModel.updateDocumentType(it)
                    },
                    onChangeAppointmentType = {
                        appointmentViewModel.updateAppointmentType(it)
                    },
                    onChangeRequestedDate = {
                        appointmentViewModel.updateRequestedDate(it)
                    },
                    onChangeReason = {
                        appointmentViewModel.updateReason(it)
                    },
                    appointment = uiState.newAppointmentState!!
                )
                uiState.hasAppointment && !uiState.isModifyingAppointment -> {
                    uiState.newAppointmentState?.let { AppointmentDetailsScreen(it, paddingValues) }
                }
                !uiState.hasAppointment -> NoAppointmentMade(paddingValues)
            }
        }
    }
}

@Composable
fun NoInternetScreen(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.WifiOff,
                contentDescription = "No Internet",
                tint = Color.Red, // Adjust color if needed
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No Internet Connection",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Please check your connection and try again.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
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
