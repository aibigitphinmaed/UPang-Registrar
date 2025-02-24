package com.ite393group5.services

import com.ite393group5.dto.ImageRecord
import com.ite393group5.dto.UserProfile
import com.ite393group5.models.LocationInfo
import com.ite393group5.models.PersonalInfo
import com.ite393group5.models.Updatable
import com.ite393group5.models.User

interface UserService {

    suspend fun findByUsername(username: String): User?
    suspend fun findByUsernameAndPassword(user:User):User?
    suspend fun register(user:User,locationInfo: LocationInfo,personalInfo: PersonalInfo): User
    suspend fun <T : Updatable> update(data:T,user:User):Boolean
    suspend fun logout(user:User, token:String):Boolean
    suspend fun retrieveProfileById(userid:Int): PersonalInfo?
    suspend fun retrieveAddressById(userid:Int):LocationInfo?
    suspend fun <T: Updatable> updateProfile(data:T,username: String):Boolean

    suspend fun registerStudent(user:User, userProfile: UserProfile):User


    suspend fun getStudents():List<User>
    suspend fun updateProfileImageRecords(fileName:String, username:String): Boolean
    suspend fun getImageRecordById(imageId: Int): ImageRecord?
    suspend fun getCurrentUserProfileImageId(username:String):Int?

}
