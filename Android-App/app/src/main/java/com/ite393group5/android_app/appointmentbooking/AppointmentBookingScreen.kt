package com.ite393group5.android_app.appointmentbooking

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.ite393group5.android_app.utilities.CustomAppTopbar
import dagger.hilt.android.lifecycle.HiltViewModel


@Composable
fun AppointmentBookingScreen(
    modifier: Modifier,
    openDrawer: () -> Unit,
    appointmentBookingViewModel: AppointmentBookingViewModel = hiltViewModel<AppointmentBookingViewModel>()
) {

    val stateAPS by appointmentBookingViewModel.stateAPS.collectAsState() //read only di mo pwede i edit ito dito or iset yun value nya dito sa screen file kt mo

    Scaffold(
        topBar = {
            CustomAppTopbar("Appointment Booking", openDrawer, modifier = modifier)
        },
        bottomBar = {},
        floatingActionButton = {},
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.End,
        containerColor = Color.White,
        contentColor = Color.White,
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .windowInsetsPadding(WindowInsets.systemBars),
            color = Color.White,
            contentColor = Color.White,
        ) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column {

                    Text("Appointment Booking Screen", color = Color.Black)

                    Button(onClick = {
                        appointmentBookingViewModel.onClicked()
                    }) {
                        Text("Click Me")
                    }
                    Text(stateAPS.token, color = Color.Black)
                    if (stateAPS.isClicked) {
                        Text("Clicked", color = Color.Black)

                    } else {
                        Text("Not Clicked", color = Color.Black)
                    }
                }
            }
        }


    }


}

