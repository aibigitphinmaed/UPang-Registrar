package com.ite393group5.services

import com.ite393group5.dto.StudentProfile

interface StudentService {
    suspend fun updateProfile(studentProfile: StudentProfile)
}