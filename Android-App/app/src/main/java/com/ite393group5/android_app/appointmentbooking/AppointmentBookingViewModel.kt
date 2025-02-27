package com.ite393group5.android_app.appointmentbooking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ite393group5.android_app.appointmentbooking.state.AppointmentScreenState
import com.ite393group5.android_app.services.local.LocalServiceImpl
import com.ite393group5.android_app.services.remote.RemoteServiceImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AppointmentBookingViewModel @Inject constructor(
    localServiceImpl: LocalServiceImpl,
    remoteServiceImpl: RemoteServiceImpl
) : ViewModel() {

    private val _stateAppointmentView = MutableStateFlow(AppointmentScreenState())
    val stateAppointmentView: StateFlow<AppointmentScreenState> = _stateAppointmentView

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()


    //region creating new appointment
    //this function is called when the user clicks on the create appointment button


    fun createAppointmentRequest() {
        _stateAppointmentView.value = _stateAppointmentView.value.copy(
            isCreatingAppointment = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            _toastMessage.emit("Creating New Appointment")
        }

    }

    fun firstConfirmationRequest() {
        _stateAppointmentView.value = _stateAppointmentView.value.copy(
            firstConfirmation = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            _toastMessage.emit("Confirming New Appointment")
        }
    }

    fun cancelOnFirstConfirmationRequest() {
        _stateAppointmentView.value = _stateAppointmentView.value.copy(
            isCreatingAppointment = false
        )
    }

    fun goBackToCreateAppointment() {
        _stateAppointmentView.value = _stateAppointmentView.value.copy(
        firstConfirmation = false
        )
    }

    fun confirmAppointmentRequest() {
        viewModelScope.launch(Dispatchers.IO) {
            _toastMessage.emit("Last Confirmation To Create Appointment Complete")
            _stateAppointmentView.value = _stateAppointmentView.value.copy(
                isCreatingAppointment = false,
                firstConfirmation = false
            )
        }
    }

    //endregion

}

