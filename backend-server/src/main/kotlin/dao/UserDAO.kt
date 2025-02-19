package com.ite393group5.dao

import com.ite393group5.dto.ImageRecord
import com.ite393group5.models.LocationInfo
import com.ite393group5.models.PersonalInfo
import com.ite393group5.models.User

interface UserDAO {
    suspend fun createUser(user: User, locationInfo: LocationInfo?, personalInfo: PersonalInfo?): User
    suspend fun updateUsername(user: User): User
    suspend fun deleteUser(user: User)
    suspend fun findUser(user: User): User?
    suspend fun findUserByUsernameAndPassword(user: User): User?

    suspend fun updatePassword(user: User, newPassword: String): Boolean
    suspend fun updatePersonalInfo(user: User, personalInfo: PersonalInfo?): Boolean
    suspend fun updateLocationInfo(user: User, locationInfo: LocationInfo?): Boolean

    suspend fun findByUsername(username: String): User?
    suspend fun retrievePersonalInformation(userId: Int): PersonalInfo?
    suspend fun retrieveAddressInformation(userId: Int): LocationInfo?
    suspend fun getAllStudents(): List<User>


    suspend fun updateUser(userid:Int?, data:User): Boolean
    suspend fun recordImage(user:User, fileName:String): Boolean
    suspend fun getImageRecord(imageId: Int): ImageRecord?
    suspend fun getCurrentUserProfileId(string: String): Int?

}