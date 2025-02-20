package com.ite393group5.android_app.profilemanagement.domain

import com.ite393group5.android_app.models.LocationInfo
import com.ite393group5.android_app.models.PersonalInfo
import com.ite393group5.android_app.services.local.LocalService
import com.ite393group5.android_app.services.remote.RemoteService
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


class ProfileUpdateUseCase @Inject constructor(
    private val remoteService: RemoteService,
    private val localService: LocalService
){
    suspend fun executeProfileUpdate(personalInfo: PersonalInfo, locationInfo: LocationInfo, profileImageLocation: String): HttpStatusCode =
        withContext(Dispatchers.IO) {
            try {
                val response = remoteService.updatePersonalInformation(personalInfo, locationInfo,profileImageLocation)
                if (response == HttpStatusCode.OK) {
                    saveUpdatedInfo(personalInfo, locationInfo, profileImageLocation)
                    Timber.d("Profile successfully updated")
                } else {
                    Timber.w("Profile update failed: $response")
                }
                response
            } catch (e: Exception) {
                Timber.e(e, "Profile update request failed")
                HttpStatusCode.InternalServerError
            }
        }

    /**
     * Saves the updated profile data to local storage.
     */
    private suspend fun saveUpdatedInfo(personalInfo: PersonalInfo, locationInfo: LocationInfo, profileImageLocation: String) {
        localService.savePersonalInfo(personalInfo)
        localService.saveAddressInfo(locationInfo)
        localService.saveProfileImageLocation(profileImageLocation)
        Timber.d("Updated profile information saved locally")
    }


}