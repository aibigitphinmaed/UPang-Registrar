package com.ite393group5.android_app.profilemanagement.domain

import com.ite393group5.android_app.models.LocationInfo
import com.ite393group5.android_app.models.PersonalInfo
import com.ite393group5.android_app.services.local.LocalService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ProfileUseCase @Inject constructor(
    private val localService: LocalService
){
    fun getPersonalInfo(): Flow<PersonalInfo>{
        return localService.flowPersonalInfo
    }
    fun getLocationInfo(): Flow<LocationInfo> {
        return localService.flowAddressInfo
    }

}