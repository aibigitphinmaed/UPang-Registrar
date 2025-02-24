package com.ite393group5.dao

import com.ite393group5.dto.ImageRecord
import com.ite393group5.dto.user.UserProfile
import com.ite393group5.models.LocationInfo
import com.ite393group5.models.PersonalInfo
import com.ite393group5.models.User

interface UserDAO {

    fun createUser(user: User, locationInfo: LocationInfo?, personalInfo: PersonalInfo?): User
    fun getUserProfileById(userId:Int): UserProfile?
    fun updateUser(userId: Int?, updatedUser: UserProfile?): Boolean
    fun deleteUser(userId: Int): Boolean
    fun getUserIdByUsername(username: String): Int?
    fun getUserByUserId(userId:Int?): User?
    fun getAllUsers():List<User>
    fun recordImage(fileName:String, userId:Int?):Boolean
    fun getImageRecord(imageId: Int): ImageRecord?
    fun getCurrentUserProfileImageId(userId:Int): Int?
}