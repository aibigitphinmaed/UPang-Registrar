package com.ite393group5.android_app.profilemanagement.domain

import com.ite393group5.android_app.models.LocationInfo
import com.ite393group5.android_app.models.PersonalInfo
import com.ite393group5.android_app.services.local.LocalService
import com.ite393group5.android_app.services.remote.RemoteService
import io.ktor.http.HttpStatusCode
import javax.inject.Inject


class ProfileUpdateUseCase @Inject constructor(
    private val remoteService: RemoteService
){
    fun updatePersonalInfo(personalInfo: PersonalInfo): HttpStatusCode {
        return HttpStatusCode.OK
    }
    fun updateLocationInfo(locationInfo: LocationInfo): HttpStatusCode {
        return HttpStatusCode.OK
    }

}