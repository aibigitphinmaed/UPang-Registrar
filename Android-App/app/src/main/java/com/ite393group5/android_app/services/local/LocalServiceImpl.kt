package com.ite393group5.android_app.services.local

import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStore
import com.ite393group5.android_app.models.AppPreferences
import com.ite393group5.android_app.models.Token
import com.ite393group5.android_app.utilities.AppPreferencesSerializer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import javax.inject.Inject

class LocalServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocalService{
    private val corruptionHandler = ReplaceFileCorruptionHandler(
        produceNewData = { AppPreferences()}
    )

    private val Context.dataStore by dataStore(context.packageName + "_preferences.json", AppPreferencesSerializer, corruptionHandler)

    override suspend fun saveBearerToken(token: Token) {
        context.dataStore.updateData {
            it.copy(
                token = it.token?.let {
                    Token(
                        bearerToken = token.bearerToken,
                        refreshToken = token.refreshToken,
                        expirationTimeDate = token.expirationTimeDate)
                }
            )
        }
    }

    override suspend fun saveRefreshToken(token: Token) {
        context.dataStore.updateData {
            it.copy(
                token = it.token?.let {
                    Token(
                        bearerToken = token.bearerToken,
                        refreshToken = token.refreshToken,
                        expirationTimeDate = token.expirationTimeDate)
                }
            )
        }
    }

    override suspend fun getBearerToken(): String {
        return context.dataStore.data.firstOrNull()?.token?.bearerToken ?: ""
    }

    override suspend fun getRefreshToken(): String {
        return context.dataStore.data.firstOrNull()?.token?.refreshToken ?: ""
    }

    override suspend fun checkLocalDataStoreIfNotNull(): Boolean {
        val token = context.dataStore.data.firstOrNull()?.token
        val personalInfo = context.dataStore.data.firstOrNull()?.personalInfo
        val localtionInfo = context.dataStore.data.firstOrNull()?.locationInfo
        return token != null && personalInfo != null && localtionInfo != null
    }

    override suspend fun clearEverythingOnLocal() {
        context.dataStore.updateData {
            AppPreferences(
                token = null,
                personalInfo = null,
                locationInfo = null
            )
        }
    }

    override suspend fun getTokenExpiration(): LocalDate? {
        return context.dataStore.data.firstOrNull()?.token?.expirationTimeDate
    }

}
