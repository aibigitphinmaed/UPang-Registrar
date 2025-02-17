package com.ite393group5.services

import com.ite393group5.dto.StudentProfile
import java.sql.Connection
class StudentServiceImpl(
    private val dbConnection: Connection
) : StudentService {
    override suspend fun updateProfile(studentProfile: StudentProfile) {

    }

}