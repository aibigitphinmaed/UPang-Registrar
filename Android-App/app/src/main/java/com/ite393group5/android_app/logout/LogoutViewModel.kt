package com.ite393group5.android_app.logout

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ite393group5.android_app.services.local.LocalServiceImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val localServiceImpl: LocalServiceImpl
) : ViewModel() {

    private val _confirmState = MutableStateFlow(false) //ito yun real holder ng value
    val confirmState: StateFlow<Boolean> = _confirmState //read only lang ito
    private val _clearingState = MutableStateFlow(false)
    val clearingState: StateFlow<Boolean> = _clearingState

    fun startClearingDataStore() {
        _confirmState.value = true

        viewModelScope.launch {
            _clearingState.value = true
            try {
                localServiceImpl.clearEverythingOnLocal()
                _clearingState.value = false
            } catch (e: Exception) {
                Timber.tag("LogoutViewModel").e(e)
            }

        }
    }


}