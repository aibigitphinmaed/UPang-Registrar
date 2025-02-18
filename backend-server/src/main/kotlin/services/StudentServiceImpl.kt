package com.ite393group5.services

import com.ite393group5.dto.UserProfile
import java.sql.Connection
class StudentServiceImpl(
    private val dbConnection: Connection
) : StudentService {
    override suspend fun updateProfile(userProfile: UserProfile) {

    }

}