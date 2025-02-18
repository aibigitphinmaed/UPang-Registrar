package com.ite393group5.android_app.loadingscreen

import androidx.compose.runtime.mutableStateOf
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
class LoadingViewModel @Inject constructor(
    private val localServiceImpl: LocalServiceImpl,
    private val remoteServiceImpl: RemoteServiceImpl
):ViewModel() {

    private val _loadingState = MutableStateFlow(LoadingState())
    val flowLoadingState: StateFlow<LoadingState> = _loadingState
    init{
        viewModelScope.launch(Dispatchers.IO) {
            if(localServiceImpl.checkSession()){
                Timber.tag("LoadingViewModel").e("Session found")
                Timber.tag("LoadingViewModel").e(localServiceImpl.getBearerToken())
                Timber.tag("LoadingViewModel").e(localServiceImpl.getPersonalInfo().toString())
                Timber.tag("LoadingViewModel").e(localServiceImpl.getAddressInfo().toString())
                Timber.tag("LoadingViewModel").e(localServiceImpl.getUserId().toString())
                _loadingState.value = _loadingState.value.copy(
                    waitingUserId = false,
                    waitingForProfileData = false,
                    waitingForAddressData = false
                )
            }else{
                Timber.tag("LoadingViewModel").e("No session found")
                // no session save in localServiceImpl dataStore
                // such as no token or token expired
                // we shall remotely get the AppPreferences data via remotely
                try{
                    remoteServiceImpl.retrievePreferences()
                    if(localServiceImpl.checkLocalDataStoreIfNotNull()){
                        Timber.tag("LoadingViewModel").e("All data retrieved")
                        _loadingState.value = _loadingState.value.copy(
                            waitingUserId = false,
                            waitingForProfileData = false,
                            waitingForAddressData = false
                        )
                    }else{
                        Timber.tag("LoadingViewModel").e("Not all data retrieved")
                        _loadingState.value = _loadingState.value.copy(
                            waitingUserId = true,
                            waitingForProfileData = true,
                            waitingForAddressData = true)
                    }
                }catch (e:Exception){
                    Timber.tag("LoadingViewModel").e(e)
                }
            }
        }.invokeOnCompletion {
            cause ->
            //probably internet got cut-off, timeouts or the user suddenly close the app
            //we gonna clear everything here for smooth transition to the start
            if(cause != null){
                viewModelScope.launch {
                    localServiceImpl.clearEverythingOnLocal()
                }
                Timber.tag("LoadingViewModel").e(cause)
            }
        }

    }
}