package com.ite393group5.android_app.profilemanagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ite393group5.android_app.models.LocationInfo
import com.ite393group5.android_app.models.PersonalInfo
import com.ite393group5.android_app.profilemanagement.domain.ProfileUseCase
import com.ite393group5.android_app.profilemanagement.state.ProfileScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    private val profileUpdateUseCase: ProfileUpdateUseCase
) :ViewModel(){
    private val _mutableStateProfile = MutableStateFlow(ProfileScreenState())
    val flowProfileState: StateFlow<ProfileScreenState> = _mutableStateProfile

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
           try{
               profileUseCase.getPersonalInfo().collect { personalInfo ->
                   profileUseCase.getLocationInfo().collect { locationInfo ->
                       _mutableStateProfile.value = ProfileScreenState(
                           personalInfo = personalInfo,
                           locationInfo = locationInfo
                       )
                   }
               }
           }catch (e:Exception){
               Timber.tag("ProfileScreenViewModel").e(e)
           }
        }
    }
    fun showConfirmWindow(personalInfo: PersonalInfo?, locationInfo: LocationInfo?) {
        _mutableStateProfile.value = _mutableStateProfile.value.copy(
            editMode = false,
            showConfirmWindow = true,
            personalInfo = personalInfo,
            locationInfo = locationInfo
        )
    }
    fun startEditMode(){
        _mutableStateProfile.value = _mutableStateProfile.value.copy(
            editMode = true,
            showConfirmWindow = false
        )
    }

    fun cancelEditMode(){
        _mutableStateProfile.value = _mutableStateProfile.value.copy(
            editMode = false,
            showConfirmWindow = false
        )
    }

    fun completeEditing(){
        viewModelScope.launch {
            _mutableStateProfile.value = _mutableStateProfile.value.copy(
                editMode = false,
                showConfirmWindow = false
            )


        }
    }


}

