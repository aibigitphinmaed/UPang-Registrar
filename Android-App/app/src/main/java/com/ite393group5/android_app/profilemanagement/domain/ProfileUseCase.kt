package com.ite393group5.android_app.profilemanagement.domain

import android.graphics.Bitmap
import com.ite393group5.android_app.models.LocationInfo
import com.ite393group5.android_app.models.PersonalInfo
import com.ite393group5.android_app.services.local.LocalService
import com.ite393group5.android_app.services.remote.RemoteService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


class ProfileUseCase @Inject constructor(
    private val localService: LocalService,
    private val remoteService: RemoteService
){
     fun getPersonalInfo(): StateFlow<PersonalInfo> =
        localService.flowPersonalInfo.stateIn(
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()), // Keeps state alive
            started = SharingStarted.Lazily, // Starts only when collected
            initialValue = PersonalInfo() // Provide a default value
        )
    fun getLocationInfo(): StateFlow<LocationInfo> = localService.flowAddressInfo.stateIn(
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob()), // Keeps state alive
        started = SharingStarted.Lazily, // Starts only when collected
        initialValue = LocationInfo() // Provide a default value
    )

    suspend fun getProfileImage():Bitmap? {
        return remoteService.getProfileImage()
    }



}