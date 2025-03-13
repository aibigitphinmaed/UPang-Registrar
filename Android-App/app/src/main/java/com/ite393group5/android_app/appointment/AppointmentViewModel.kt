package com.ite393group5.android_app.appointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ite393group5.android_app.appointment.state.AppointmentScreenState
import com.ite393group5.android_app.models.Appointment
import com.ite393group5.android_app.utilities.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _hasInternet = MutableStateFlow(false)  // Default to false
    val hasInternet: StateFlow<Boolean> = _hasInternet.asStateFlow()


    private val _stateAppointmentView = MutableStateFlow(AppointmentScreenState())
    val stateAppointmentView: StateFlow<AppointmentScreenState> = _stateAppointmentView

    init {
        // Observe network connectivity
        viewModelScope.launch {
            networkMonitor.isConnected.collectLatest { isOnline ->
                _hasInternet.value = isOnline
                _stateAppointmentView.value =
                    _stateAppointmentView.value.copy(hasInternet = isOnline)
            }
        }
        viewModelScope.launch {
            appointmentRepository.getCurrentAppointment()?.let {
                _stateAppointmentView.value = _stateAppointmentView.value.copy(
                    hasAppointment = true,
                    newAppointmentState = it
                )
            }
        }
    }

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()


    //region creating new appointment
    //this function is called when the user clicks on the create appointment button
    fun createAppointmentRequest() {
        if (_hasInternet.value) {
            _stateAppointmentView.value =
                _stateAppointmentView.value.copy(isCreatingAppointment = true)
        } else {
            viewModelScope.launch { _toastMessage.emit("⚠️ No internet connection. Please try again later.") }
        }
    }

    fun firstConfirmationRequest() {

        viewModelScope.launch(Dispatchers.IO) {

            if (_stateAppointmentView.value.appointmentRequest == null) {
                _toastMessage.emit("Appointment Request is null")
                return@launch
            }

            _stateAppointmentView.value = _stateAppointmentView.value.copy(
                firstConfirmation = true
            )
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

            if (_stateAppointmentView.value.newAppointmentState == null) {
                val appointmentRequest = _stateAppointmentView.value.appointmentRequest
                Timber.tag("textCAR").e("ViewModel: %s", appointmentRequest.toString())
                val response = appointmentRepository.requestNewAppointment(appointmentRequest)
                Timber.tag("cAR").e("appointment block inside")
                when (response.value) {
                    200 -> {

                        _stateAppointmentView.value = _stateAppointmentView.value.copy(
                            isCreatingAppointment = false,
                            firstConfirmation = false,
                            hasAppointment = true
                        )

                        appointmentRepository.getCurrentAppointment()?.let {
                            _stateAppointmentView.value = _stateAppointmentView.value.copy(
                                hasAppointment = true,
                                newAppointmentState = it
                            )
                        }
                        _toastMessage.emit("Created New Appointment")
                    }

                    409 -> {
                        _stateAppointmentView.value =
                            _stateAppointmentView.value.copy(isCreatingAppointment = false)
                        _toastMessage.emit("⚠️ Appointment already exists")
                    }

                    else -> {
                        _stateAppointmentView.value =
                            _stateAppointmentView.value.copy(isCreatingAppointment = false)
                        _toastMessage.emit("⚠️ Error creating appointment")
                    }
                }
            }
            _toastMessage.emit("Last Confirmation To Create Appointment Complete")
            _stateAppointmentView.value = _stateAppointmentView.value.copy(
                isCreatingAppointment = false,
                firstConfirmation = false,
            )
        }
    }

    //endregion


    fun updateDocumentType(it: String) {

        _stateAppointmentView.value = _stateAppointmentView.value.copy(
            appointmentRequest = _stateAppointmentView.value.appointmentRequest.copy(
                documentType = it
            )
        )
        Timber.tag("cAR")
            .e("%s is updated", _stateAppointmentView.value.appointmentRequest.toString())
    }

    fun updateAppointmentType(it: String) {
        _stateAppointmentView.value = _stateAppointmentView.value.copy(
            appointmentRequest = _stateAppointmentView.value.appointmentRequest.copy(
                appointmentType = it
            )
        )
        Timber.tag("cAR")
            .e("%s is updated", _stateAppointmentView.value.appointmentRequest.toString())
    }

    fun updateRequestedDate(it: String) {
        _stateAppointmentView.value = _stateAppointmentView.value.copy(
            appointmentRequest = _stateAppointmentView.value.appointmentRequest.copy(
                requestedDate = it
            )
        )
        Timber.tag("cAR")
            .e("%s is updated", _stateAppointmentView.value.appointmentRequest.toString())
    }
private var originalState: Appointment? = null
    fun modifyCurrentAppointment() {
        originalState = _stateAppointmentView.value.newAppointmentState?.copy()
       _stateAppointmentView.value = _stateAppointmentView.value.copy(
           isModifyingAppointment = true
       )
    }

    fun cancelModifyCurrentAppointment() {
        _stateAppointmentView.value = _stateAppointmentView.value.copy(
            isModifyingAppointment = false
        )
    }

    fun cancelCurrentAppointment() {
        _stateAppointmentView.value = _stateAppointmentView.value.copy(
            cancellingCurrentAppointment = true
        )
    }

    fun finalizeModifyCurrentAppointment() {
       viewModelScope.launch {
           val appointment = _stateAppointmentView.value.newAppointmentState
           val response = appointment?.let { appointmentRepository.modifyCurrentAppointment(it) }
           if (response != null) {
               when(response.value) {
                   200 -> {
                       _stateAppointmentView.value = _stateAppointmentView.value.copy(
                           isModifyingAppointment = false
                       )
                       _toastMessage.emit("Appointment Modified")
                   }

                   404 -> {
                       _stateAppointmentView.value = _stateAppointmentView.value.copy(
                           isModifyingAppointment = false
                       )
                       _toastMessage.emit("Appointment Not Found")
                   }

                   else -> {
                       _stateAppointmentView.value = _stateAppointmentView.value.copy(
                           isModifyingAppointment = false
                       )
                       _toastMessage.emit("Error Modifying Appointment")
                   }
               }
           }
       }
    }

    fun updateReason(it: String) {
        _stateAppointmentView.value = _stateAppointmentView.value.copy(
            newAppointmentState = _stateAppointmentView.value.newAppointmentState?.copy(
                reason = it
            )
        )
    }


}

