package com.ite393group5.android_app.services.local

import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.ite393group5.android_app.models.AppPreferences
import com.ite393group5.android_app.models.LocationInfo
import com.ite393group5.android_app.models.PersonalInfo
import com.ite393group5.android_app.models.Token
import com.ite393group5.android_app.utilities.AppPreferencesSerializer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

private val corruptionHandler = ReplaceFileCorruptionHandler(
    produceNewData = { AppPreferences(
        userId = null,
        token = null,
        personalInfo = null,
        locationInfo = null
    )}
)
private val Context.appPreferencesDataStore by dataStore( "app_preferences.json", AppPreferencesSerializer, corruptionHandler)

class LocalServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocalService{

    private val dataStore = context.appPreferencesDataStore

    init{
        CoroutineScope(Dispatchers.IO).launch {
            val appPrefs = collectFromDataStore()
            appPrefs?.let {
                _mutableUserId.emit(it.userId ?: -1)
                _mutableToken.emit(it.token ?: Token())
                _mutablePersonalInfo.emit(it.personalInfo ?: PersonalInfo())
                _mutableAddressInfo.emit(it.locationInfo ?: LocationInfo())
            }
            Timber.tag("LocalServiceImpl").e("Emitting PersonalInfo: ${_mutablePersonalInfo.value}")
            Timber.tag("LocalServiceImpl").e("Emitting LocationInfo: ${_mutableAddressInfo.value}")
        }
    }


    private val _mutableUserId = MutableStateFlow(-1)
    override val flowUserId:StateFlow<Int> = _mutableUserId

    private val _mutableToken = MutableStateFlow(Token())
    override val flowToken: StateFlow<Token> = _mutableToken

    private val _mutablePersonalInfo = MutableStateFlow(PersonalInfo())
   override val flowPersonalInfo: StateFlow<PersonalInfo> = _mutablePersonalInfo

    private val _mutableAddressInfo = MutableStateFlow(LocationInfo())
     override val flowAddressInfo:StateFlow<LocationInfo> = _mutableAddressInfo



    suspend fun collectFromDataStore():AppPreferences? = withContext(Dispatchers.IO){
        return@withContext try {
            val appPrefs = dataStore.data.firstOrNull()
            Timber.tag("LocalServiceImpl").e(appPrefs.toString())

            appPrefs?.let {
                _mutableToken.value = _mutableToken.value.copy(
                    bearerToken = it.token?.bearerToken ?: "",
                    refreshToken = it.token?.refreshToken ?: "",
                    expirationTimeDate = it.token?.expirationTimeDate ?: LocalDate.now()
                )
                _mutableUserId.value = it.userId ?: -1
            }
            appPrefs
        } catch (e: Exception) {
            Timber.tag("LocalServiceImpl").e(e)
            null
        }
    }


    override suspend fun saveToDateStore(): Unit = withContext(Dispatchers.IO){
        try {
            dataStore.updateData {
                it.copy(
                    userId = flowUserId.value,
                    token = flowToken.value,
                    personalInfo = flowPersonalInfo.value,
                    locationInfo = flowAddressInfo.value
                )
            }
            Timber.tag("DataStoreImpl").e("Saved to Datastore")
        }catch (e:Exception){
            Timber.tag("DataStoreImpl").e(e)
        }

    }


    override suspend fun saveBearerToken(token: Token) {

        _mutableToken.value = _mutableToken.value.copy(
            bearerToken = token.bearerToken,
            refreshToken = token.refreshToken,
            expirationTimeDate = token.expirationTimeDate
        )

    }


    override suspend fun saveRefreshToken(token: Token) {
        _mutableToken.value = _mutableToken.value.copy(
            bearerToken = token.bearerToken,
            refreshToken = token.refreshToken,
            expirationTimeDate = token.expirationTimeDate
        )
    }

    override suspend fun getBearerToken(): String {
        return flowToken.value.bearerToken!!
    }

    override suspend fun getRefreshToken(): String {
        return flowToken.value.refreshToken!!
    }

    override suspend fun checkLocalDataStoreIfNotNull(): Boolean = withContext(Dispatchers.IO){
        val userId = dataStore.data.firstOrNull()?.userId
        val token = dataStore.data.firstOrNull()?.token
        val personalInfo = dataStore.data.firstOrNull()?.personalInfo
        val locationInfo = dataStore.data.firstOrNull()?.locationInfo
        return@withContext token != null && personalInfo != null && locationInfo != null && userId != null
    }

    override suspend fun clearEverythingOnLocal() {
        dataStore.updateData {
            AppPreferences(
                userId = null,
                token = null,
                personalInfo = null,
                locationInfo = null
            )
        }
        collectFromDataStore()
    }

    override suspend fun getTokenExpiration(): LocalDate? {
        return flowToken.value.expirationTimeDate
    }

    override suspend fun checkSession(): Boolean {
        return (flowToken.value.expirationTimeDate?.isAfter(LocalDate.now()) ?: false) && flowUserId.value >= 0
    }

    override suspend fun saveUserId(userId: Int) {
        _mutableUserId.value = userId
    }

    override suspend fun getUserid(): Int? {
        return if(flowUserId.value < 0) null else flowUserId.value
    }

    override suspend fun savePersonalInfo(personalInfo: PersonalInfo) {
        _mutablePersonalInfo.value = _mutablePersonalInfo.value.copy(
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
        _mutableAddressInfo.value = _mutableAddressInfo.value.copy(
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
        return flowPersonalInfo.value
    }

    override suspend fun getAddressInfo(): LocationInfo {
        return flowAddressInfo.value
    }
    override suspend fun getUserId(): Int? {
        return if(flowUserId.value < 0) null else flowUserId.value
    }
}
