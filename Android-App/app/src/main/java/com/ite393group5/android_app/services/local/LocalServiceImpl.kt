package com.ite393group5.android_app.services.local

import android.content.Context
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import com.ite393group5.android_app.models.AppPreferences
import com.ite393group5.android_app.models.LocationInfo
import com.ite393group5.android_app.models.PersonalInfo
import com.ite393group5.android_app.models.Token
import com.ite393group5.android_app.utilities.AppPreferencesSerializer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

class LocalServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocalService{
    private val corruptionHandler = ReplaceFileCorruptionHandler(
        produceNewData = { AppPreferences(
            userId = null,
            token = null,
            personalInfo = null,
            locationInfo = null
        )}
    )

    private val Context.dataStore by dataStore(context.packageName + "_preferences.json", AppPreferencesSerializer, corruptionHandler)
    private val mutableUserId = mutableIntStateOf(-1)
    private val mutableToken = mutableStateOf(Token("","", LocalDate.now()))
    private val mutablePersonalInfo = mutableStateOf(PersonalInfo(
        firstName = "",
        lastName = "",
        middleName = "",
        extensionName = "",
        gender = "",
        citizenship = "",
        religion = "",
        civilStatus = "",
        email = "",
        number = "",
        birthDate = null,
        fatherName = "",
        motherName = "",
        spouseName = "",
        contactPersonNumber = ""
    ))

    private val mutableAddressInfo = mutableStateOf(
        LocationInfo(
            houseNumber = "",
            street = "",
            zone = "",
            barangay = "",
            cityMunicipality = "",
            province = "",
            country = "",
            postalCode = ""
        )
    )


    suspend fun collectFromDataStore():AppPreferences? = withContext(Dispatchers.IO){
        return@withContext try{
            val apprefs =  context.dataStore.data.firstOrNull()
            Timber.tag("LocalServiceImpl").e(apprefs.toString())


            if (apprefs != null) {
                mutableToken.value = mutableToken.value.copy(
                    bearerToken = apprefs.token?.bearerToken ?: "",
                    refreshToken = apprefs.token?.refreshToken ?: "",
                    expirationTimeDate = apprefs.token?.expirationTimeDate ?: LocalDate.now()
                )
                mutableUserId.intValue = apprefs.userId ?: -1

                mutableAddressInfo.value = mutableAddressInfo.value.copy(
                    houseNumber = apprefs.locationInfo?.houseNumber ?: "",
                    street = apprefs.locationInfo?.street ?: "",
                    zone = apprefs.locationInfo?.zone ?: "",
                    barangay = apprefs.locationInfo?.barangay ?: "",
                    cityMunicipality = apprefs.locationInfo?.cityMunicipality ?: "",
                    province = apprefs.locationInfo?.province ?: "",
                    country = apprefs.locationInfo?.country ?: "",
                    postalCode = apprefs.locationInfo?.postalCode ?: ""
                )
                mutablePersonalInfo.value = mutablePersonalInfo.value.copy(
                    firstName = apprefs.personalInfo?.firstName ?: "",
                    lastName = apprefs.personalInfo?.lastName ?: "",
                    middleName = apprefs.personalInfo?.middleName ?: "",
                    gender = apprefs.personalInfo?.gender ?: "",
                    extensionName = apprefs.personalInfo?.extensionName ?: "",
                    citizenship = apprefs.personalInfo?.citizenship ?: "",
                    religion = apprefs.personalInfo?.religion ?: "",
                    civilStatus = apprefs.personalInfo?.civilStatus ?: "",
                    email = apprefs.personalInfo?.email ?: "",
                    birthDate = apprefs.personalInfo?.birthDate ?: LocalDate.now(),
                    number = apprefs.personalInfo?.number ?: "",
                    contactPersonNumber = apprefs.personalInfo?.contactPersonNumber ?: ""
                )
            }
           apprefs
        }catch (e:Exception){
            Timber.tag("LocalServiceImpl").e(e)
            null
        }
    }

    override suspend fun saveToDateStore(): Unit = withContext(Dispatchers.IO){
        try {
            context.dataStore.updateData {
                it.copy(
                    userId = mutableUserId.intValue,
                    token = mutableToken.value,
                    personalInfo = mutablePersonalInfo.value,
                    locationInfo = mutableAddressInfo.value
                )
            }
            Timber.tag("DataStoreImpl").e("Saved to Datastore")
        }catch (e:Exception){
            Timber.tag("DataStoreImpl").e(e)
        }

    }


    override suspend fun saveBearerToken(token: Token) {

        mutableToken.value = mutableToken.value.copy(
            bearerToken = token.bearerToken,
            refreshToken = token.refreshToken,
            expirationTimeDate = token.expirationTimeDate
        )

    }


    override suspend fun saveRefreshToken(token: Token) {
        mutableToken.value = mutableToken.value.copy(
            bearerToken = token.bearerToken,
            refreshToken = token.refreshToken,
            expirationTimeDate = token.expirationTimeDate
        )
    }

    override suspend fun getBearerToken(): String {
        return mutableToken.value.bearerToken
    }

    override suspend fun getRefreshToken(): String {
        return mutableToken.value.refreshToken
    }

    override suspend fun checkLocalDataStoreIfNotNull(): Boolean = withContext(Dispatchers.IO){
        val userId = context.dataStore.data.firstOrNull()?.userId
        val token = context.dataStore.data.firstOrNull()?.token
        val personalInfo = context.dataStore.data.firstOrNull()?.personalInfo
        val locationInfo = context.dataStore.data.firstOrNull()?.locationInfo
        return@withContext token != null && personalInfo != null && locationInfo != null && userId != null
    }

    override suspend fun clearEverythingOnLocal() {
        context.dataStore.updateData {
            AppPreferences(
                userId = null,
                token = null,
                personalInfo = null,
                locationInfo = null
            )
        }
        mutableUserId.intValue = -1

        mutableAddressInfo.value = mutableAddressInfo.value.copy(
            houseNumber =  "",
            street =  "",
            zone =  "",
            barangay =  "",
            cityMunicipality =  "",
            province =  "",
            country = "",
            postalCode =  ""
        )
        mutablePersonalInfo.value = mutablePersonalInfo.value.copy(
            firstName =  "",
            lastName =  "",
            middleName =  "",
            gender =  "",
            extensionName =  "",
            citizenship =  "",
            religion =  "",
            civilStatus = "",
            email =  "",
            birthDate = null,
            number =  "",
            contactPersonNumber = ""
        )
    }

    override suspend fun getTokenExpiration(): LocalDate? {
        return mutableToken.value.expirationTimeDate
    }

    override suspend fun checkSession(): Boolean {
        return (mutableToken.value.expirationTimeDate?.isAfter(LocalDate.now()) ?: false) && mutableUserId.intValue >= 0
    }

    override suspend fun saveUserId(userId: Int) {
        mutableUserId.intValue = userId
    }

    override suspend fun getUserid(): Int? {
        return if(mutableUserId.intValue < 0) null else mutableUserId.intValue
    }

    override suspend fun savePersonalInfo(personalInfo: PersonalInfo) {
        mutablePersonalInfo.value = mutablePersonalInfo.value.copy(
            firstName = personalInfo.firstName,
            lastName = personalInfo.lastName,
            middleName = personalInfo.middleName,
            gender = personalInfo.gender,
            citizenship = personalInfo.citizenship,
            email = personalInfo.email,
            number = personalInfo.number,
            birthDate = personalInfo.birthDate,
            fatherName = personalInfo.fatherName,
            motherName = personalInfo.motherName,
            contactPersonNumber = personalInfo.contactPersonNumber,
            spouseName = personalInfo.spouseName,
            civilStatus = personalInfo.civilStatus,
            religion = personalInfo.religion,
        )
    }

    override suspend fun saveAddressInfo(locationInfo: LocationInfo) {
        mutableAddressInfo.value = mutableAddressInfo.value.copy(
            houseNumber = locationInfo.houseNumber,
            street = locationInfo.street,
            zone = locationInfo.zone,
            barangay = locationInfo.barangay,
            cityMunicipality = locationInfo.cityMunicipality,
            province = locationInfo.province,
            country = locationInfo.country,
            postalCode = locationInfo.postalCode
        )
    }

    override suspend fun getPersonalInfo(): PersonalInfo {
        return mutablePersonalInfo.value
    }

    override suspend fun getAddressInfo(): LocationInfo {
        return mutableAddressInfo.value
    }

    override suspend fun getUserId(): Int? {
        return if(mutableUserId.intValue < 0) null else mutableUserId.intValue
    }


}
