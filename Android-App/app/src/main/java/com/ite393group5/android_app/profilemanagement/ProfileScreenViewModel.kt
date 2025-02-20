package com.ite393group5.android_app.profilemanagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ite393group5.android_app.models.LocationInfo
import com.ite393group5.android_app.models.PersonalInfo
import com.ite393group5.android_app.profilemanagement.domain.ProfileChangePasswordUseCase
import com.ite393group5.android_app.profilemanagement.domain.ProfileUpdateUseCase
import com.ite393group5.android_app.profilemanagement.domain.ProfileUseCase
import com.ite393group5.android_app.profilemanagement.state.ProfileScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    private val profileUpdateUseCase: ProfileUpdateUseCase,
    private val profileChangePasswordUseCase: ProfileChangePasswordUseCase
) : ViewModel() {


    private val _profileBitmapFlow = MutableStateFlow<String?>(null)
    val profileBitmapFlow: StateFlow<String?> = _profileBitmapFlow
    private val _mutableStateProfile = MutableStateFlow(ProfileScreenState())
    val flowProfileState: StateFlow<ProfileScreenState> = _mutableStateProfile
    private var originalPersonalInfo: PersonalInfo? = null
    private var originalLocationInfo: LocationInfo? = null
    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    init {
        viewModelScope.launch {
            combine(
                profileUseCase.getPersonalInfo(),
                profileUseCase.getLocationInfo(),
                profileUseCase.getProfileImage()
            ) { personalInfo, locationInfo, profileImageLocation ->
                Timber.tag("ProfileScreenViewModel").e("Collected PersonalInfo: $personalInfo")
                Timber.tag("ProfileScreenViewModel").e("Collected LocationInfo: $locationInfo")
                originalPersonalInfo = personalInfo
                originalLocationInfo = locationInfo
                ProfileScreenState(
                    personalInfo = personalInfo,
                    locationInfo = locationInfo,
                    profileImageLocation = profileImageLocation
                )
            }.collect { state ->
                Timber.tag("ProfileScreenViewModel").e("Updating State: $state")
                _mutableStateProfile.value = state
                if (state.profileImageLocation != null) {
                    originalProfileImageLocation = state.profileImageLocation
                }
            }
        }
    }

//region profile updating state
    fun showConfirmWindow() {
        _mutableStateProfile.value = _mutableStateProfile.value.copy(
            editMode = false,
            showConfirmWindow = true,
        )
    }

    fun startEditMode() {
        originalPersonalInfo = _mutableStateProfile.value.personalInfo
        originalLocationInfo = _mutableStateProfile.value.locationInfo
        _mutableStateProfile.value = _mutableStateProfile.value.copy(editMode = true)
    }

    fun cancelEditMode() {
        _mutableStateProfile.value = _mutableStateProfile.value.copy(
            editMode = false,
            personalInfo = originalPersonalInfo ?: PersonalInfo(),
            locationInfo = originalLocationInfo ?: LocationInfo(),
            profileImageLocation = originalProfileImageLocation
        )

    }


    fun completeEditing() {
        viewModelScope.launch {
            try {
                val personalInfo = _mutableStateProfile.value.personalInfo
                val locationInfo = _mutableStateProfile.value.locationInfo
                val profileImagePath = _mutableStateProfile.value.profileImageLocation ?: ""
                if (personalInfo != null && locationInfo != null) {

                    val response = profileUpdateUseCase.executeProfileUpdate(personalInfo, locationInfo,profileImagePath)

                    if (response == HttpStatusCode.OK) {
                        _mutableStateProfile.value = _mutableStateProfile.value.copy(
                            editMode = false,
                            showConfirmWindow = false
                        )
                        _toastMessage.emit("Profile updated successfully!")
                    } else {
                        _toastMessage.emit("Failed to update profile. Please try again.")
                    }
                }
            } catch (e: Exception) {
                Timber.tag("ProfileScreenViewModel").e("Error updating profile: $e")
            }
        }
    }
    //endregion

//region Personal Info updates
        fun updateFirstName(it: String) {
            _mutableStateProfile.value = _mutableStateProfile.value.copy(
                personalInfo = _mutableStateProfile.value.personalInfo?.copy(
                    firstName = it
                )
            )
        }
    fun updateMiddleName(it: String) {
        _mutableStateProfile.value = _mutableStateProfile.value.copy(
            personalInfo = _mutableStateProfile.value.personalInfo?.copy(
                middleName = it
            )
        )
    }

        fun updateLastName(it: String) {
            _mutableStateProfile.value = _mutableStateProfile.value.copy(
                personalInfo = _mutableStateProfile.value.personalInfo?.copy(
                    lastName = it
                )
            )
        }
    fun updateExtensionName(it: String) {
        _mutableStateProfile.value = _mutableStateProfile.value.copy(
            personalInfo = _mutableStateProfile.value.personalInfo?.copy(
                extensionName = it
            )
        )
    }

    fun updatePhoneNumber(it: String) {
        _mutableStateProfile.value = _mutableStateProfile.value.copy(
            personalInfo = _mutableStateProfile.value.personalInfo?.copy(
                number = it
            )
        )
    }
    fun updateGender(newGender: String) {
        _mutableStateProfile.value = _mutableStateProfile.value.copy(
            personalInfo = _mutableStateProfile.value.personalInfo?.copy(
                gender = newGender
            )
        )
    }

    //endregion

//region Location Info updates
        fun updateStreet(it: String) {
            _mutableStateProfile.value = _mutableStateProfile.value.copy(
                locationInfo = _mutableStateProfile.value.locationInfo?.copy(
                    street = it
                )
            )
        }

        fun updateCity(it: String) {
            _mutableStateProfile.value = _mutableStateProfile.value.copy(
                locationInfo = _mutableStateProfile.value.locationInfo?.copy(
                    cityMunicipality = it
                )
            )
        }

        fun updateProvince(it: String) {
            _mutableStateProfile.value = _mutableStateProfile.value.copy(
                locationInfo = _mutableStateProfile.value.locationInfo?.copy(
                    province = it
                )
            )
        }





        fun updateHouseNumber(it: String) {
            _mutableStateProfile.value = _mutableStateProfile.value.copy(
                locationInfo = _mutableStateProfile.value.locationInfo?.copy(
                    houseNumber = it
                )
            )
        }

        fun updateZone(it: String) {
            _mutableStateProfile.value = _mutableStateProfile.value.copy(
                locationInfo = _mutableStateProfile.value.locationInfo?.copy(
                    zone = it
                )
            )
        }

        fun updateBarangay(it: String) {
            _mutableStateProfile.value = _mutableStateProfile.value.copy(
                locationInfo = _mutableStateProfile.value.locationInfo?.copy(
                    barangay = it
                )
            )
        }

        fun updateCountry(it: String) {
            _mutableStateProfile.value = _mutableStateProfile.value.copy(
                locationInfo = _mutableStateProfile.value.locationInfo?.copy(
                    country = it
                )
            )
        }

        fun updatePostalCode(it: String) {
            _mutableStateProfile.value = _mutableStateProfile.value.copy(
                locationInfo = _mutableStateProfile.value.locationInfo?.copy(
                    postalCode = it
                )
            )
        }
    //endregion

//region change password
    private var newPassword = ""
    private var confirmNewPassword = ""
    private var currentPassword =""

    fun updateVarPassword(currentPassword: String,newPassword: String, confirmNewPassword: String) {
        this.currentPassword = currentPassword
        this.newPassword = newPassword
        this.confirmNewPassword = confirmNewPassword

    }

    fun confirmChangePassword() {

        if(this.confirmNewPassword == this.newPassword){
            viewModelScope.launch {
                _toastMessage.emit("Confirm Again to finalize new password")
            }
            _mutableStateProfile.value = _mutableStateProfile.value.copy(
                confirmChangePasswordMode = true
            )
        }else{
            viewModelScope.launch {
                _toastMessage.emit("Passwords do not match")
                _mutableStateProfile.value = _mutableStateProfile.value.copy(
                    confirmChangePasswordMode = false
                )
            }
        }


    }
    fun cancelConfirmation() {
        _mutableStateProfile.value = _mutableStateProfile.value.copy(
            editMode = true,
            showConfirmWindow = false
        )
    }
    fun cancelChangePassword() {
        _mutableStateProfile.value = _mutableStateProfile.value.copy(
            changePasswordMode = false,
            confirmChangePasswordMode = false
        )
    }


    //proceed with the changing of password remotely
    fun completeChangePassword() {

        viewModelScope.launch {
            try{
                val statusCode = profileChangePasswordUseCase.executeChangePassword(currentPassword, newPassword)
                if(statusCode == HttpStatusCode.OK){
                    _toastMessage.emit("Password Updated")
                    _mutableStateProfile.value = _mutableStateProfile.value.copy(
                        changePasswordMode = false,
                        confirmChangePasswordMode = false
                    )

                }
                if(statusCode == HttpStatusCode.Unauthorized){
                    _toastMessage.emit("Failed to update password. Wrong current password.")
                    _mutableStateProfile.value = _mutableStateProfile.value.copy(
                        changePasswordMode = true,
                        confirmChangePasswordMode = false
                    )
                }

                if(statusCode == HttpStatusCode.BadRequest){
                    _toastMessage.emit("Failed to update password. Please try again later.")
                    _mutableStateProfile.value = _mutableStateProfile.value.copy(
                        changePasswordMode = true,
                        confirmChangePasswordMode = false
                    )
                }
                if(statusCode == HttpStatusCode.InternalServerError){
                    _toastMessage.emit("Failed to update password. Please try again later. Server May be down at the moment")
                    _mutableStateProfile.value = _mutableStateProfile.value.copy(
                        changePasswordMode = true,
                        confirmChangePasswordMode = false
                    )
                }
            }catch (e:Exception){
                _toastMessage.emit("Failed to update password. Please try again later.")
                Timber.tag("ProfileScreenViewModel").e("Error updating password: $e")
            }
        }

    }

    //cancel confirmation but stay in change password edit mode
    fun cancelConfirmChangePassword() {
        _mutableStateProfile.value = _mutableStateProfile.value.copy(
            changePasswordMode = true,
            confirmChangePasswordMode = false
        )
    }

    fun changePasswordMode() {
        _mutableStateProfile.value = _mutableStateProfile.value.copy(
            changePasswordMode = true
        )
    }


private var originalProfileImageLocation: String? = ""
    fun updateProfileImageLocation(profileImagePath: String) {
        Timber.tag("ProfileScreenViewModel").e("Updating profile image location: $profileImagePath")
        originalProfileImageLocation = _mutableStateProfile.value.profileImageLocation
        _mutableStateProfile.value = _mutableStateProfile.value.copy(
            profileImageLocation = profileImagePath
        )
    }




    fun updateCitizenship(it: String) {
        _mutableStateProfile.value = _mutableStateProfile.value.copy(
            personalInfo = _mutableStateProfile.value.personalInfo?.copy(
                citizenship = it
            )
        )
    }

    fun updateReligion(it: String) {
        _mutableStateProfile.value = _mutableStateProfile.value.copy(
            personalInfo = _mutableStateProfile.value.personalInfo?.copy(
                religion = it
            )
        )
    }

    fun updateCivilStatus(newStatus: String) {
        _mutableStateProfile.value = _mutableStateProfile.value.copy(
            personalInfo = _mutableStateProfile.value.personalInfo?.copy(
                civilStatus = newStatus
            )
        )
    }

    fun updateBirthDate(localDate: LocalDate?){
        if(localDate != null){
            _mutableStateProfile.value = _mutableStateProfile.value.copy(
                personalInfo = _mutableStateProfile.value.personalInfo?.copy(
                    birthDate = localDate
                )
            )
        }
    }

    fun updateFatherName(it: String) {
        _mutableStateProfile.value = _mutableStateProfile.value.copy(
            personalInfo = _mutableStateProfile.value.personalInfo?.copy(
                fatherName = it
            )
        )
    }

    fun updateMotherName(it: String) {
        _mutableStateProfile.value = _mutableStateProfile.value.copy(
            personalInfo = _mutableStateProfile.value.personalInfo?.copy(
                motherName = it
            )
        )
    }

    fun updateSpouseName(it: String) {
        _mutableStateProfile.value = _mutableStateProfile.value.copy(
            personalInfo = _mutableStateProfile.value.personalInfo?.copy(
                spouseName = it
            )
        )
    }

    fun updateContactPersonNumber(it: String) {
        _mutableStateProfile.value = _mutableStateProfile.value.copy(
            personalInfo = _mutableStateProfile.value.personalInfo?.copy(
                contactPersonNumber = it
            )
        )
    }


//endregion
}

