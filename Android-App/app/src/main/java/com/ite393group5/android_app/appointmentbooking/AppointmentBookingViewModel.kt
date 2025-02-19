package com.ite393group5.android_app.appointmentbooking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ite393group5.android_app.services.local.LocalServiceImpl
import com.ite393group5.android_app.services.remote.RemoteServiceImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class AppointmentBookingViewModel @Inject constructor(
    localServiceImpl: LocalServiceImpl,
    remoteServiceImpl: RemoteServiceImpl
) : ViewModel() {


    private val _state = MutableStateFlow(AppointmentBookingState()) // holder data or value
    val stateAPS: StateFlow<AppointmentBookingState> = _state //read only

    init{
        viewModelScope.launch(Dispatchers.IO) {

            try {
                val localToken = localServiceImpl.getBearerToken()
                _state.value = _state.value.copy(
                    token = localToken
                )
                Timber.tag("AppointmentBookingViewModel").d("Token: $localToken")
            }catch (e:Exception){
                Timber.tag("AppointmentBookingViewModel").e("Error: ${e.message}")
            }
        }
    }
    fun onClicked(){
        _state.value = _state.value.copy(
            isClicked = !_state.value.isClicked
        )
    }

}

data class AppointmentBookingState(
     val isClicked:Boolean = false,
     val token:String = ""
)