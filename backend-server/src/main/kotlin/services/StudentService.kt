package com.ite393group5.services

import com.ite393group5.dto.UserProfile

interface StudentService {
    suspend fun updateProfile(userProfile: UserProfile)
}