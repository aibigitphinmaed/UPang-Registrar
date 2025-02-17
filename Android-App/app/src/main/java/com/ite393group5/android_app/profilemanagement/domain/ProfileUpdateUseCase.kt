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
    suspend fun executeProfileUpdate(personalInfo: PersonalInfo, locationInfo: LocationInfo): HttpStatusCode =
        withContext(Dispatchers.IO) {
            try {
                val response = remoteService.updatePersonalInformation(personalInfo, locationInfo)
                if (response == HttpStatusCode.OK) {
                    saveUpdatedInfo(personalInfo, locationInfo)
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
    private suspend fun saveUpdatedInfo(personalInfo: PersonalInfo, locationInfo: LocationInfo) {
        localService.savePersonalInfo(personalInfo)
        localService.saveAddressInfo(locationInfo)
        Timber.d("Updated profile information saved locally")
    }
}