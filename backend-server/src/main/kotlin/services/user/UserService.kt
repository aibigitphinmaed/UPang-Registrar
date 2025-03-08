package com.ite393group5.services.user

import com.ite393group5.dto.user.ImageRecord
import com.ite393group5.dto.user.UserProfile
import com.ite393group5.models.LocationInfo
import com.ite393group5.models.PersonalInfo
import com.ite393group5.models.Updatable
import com.ite393group5.models.User

interface UserService {

    suspend fun findByUsername(username: String): User?

    suspend fun register(user:User,locationInfo: LocationInfo,personalInfo: PersonalInfo): User
    suspend fun <T : Updatable> updateProfile(data:T,user:User):Boolean
    suspend fun logout(user:User, token:String):Boolean
    suspend fun getStudents():List<User>
    suspend fun updateProfileImage(fileName:String, username:String): Boolean
    suspend fun getImageRecordById(imageId: Int): ImageRecord?
    suspend fun getCurrentUserProfileImageId(userId:Int):Int?

    suspend fun getUserProfile(userId:Int):UserProfile?

}
