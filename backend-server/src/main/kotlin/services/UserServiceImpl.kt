package com.ite393group5.services

import com.ite393group5.dao.UserDAOImpl
import com.ite393group5.dto.ImageRecord
import com.ite393group5.dto.user.UserProfile
import com.ite393group5.models.LocationInfo
import com.ite393group5.models.PersonalInfo
import com.ite393group5.models.Updatable
import com.ite393group5.models.User

class UserServiceImpl() : UserService {

    val userDAO = UserDAOImpl()
    override suspend fun findByUsername(username: String): User? {
        val userId = userDAO.getUserIdByUsername(username)
        if (userId != null){
            return userDAO.getUserByUserId(userId)
        }
        return null
    }
    
    override suspend fun register(
        user: User,
        locationInfo: LocationInfo,
        personalInfo: PersonalInfo
    ): User {
        return userDAO.createUser(user, locationInfo, personalInfo)
    }

    //Update function for LocationInfo and PersonalInfo
    //Some can be added
    override suspend fun <T : Updatable> updateProfile(data: T, user: User): Boolean {
        val foundUser = userDAO.getUserByUserId(user.id)
        return if (foundUser != null) {
            when (data) {
                is LocationInfo -> {
                    val userProfile = UserProfile(
                        userAddressInfo = data
                    )
                    userDAO.updateUser(user.id,userProfile)
                }

                is PersonalInfo -> {
                    val userProfile = UserProfile(
                        userPersonalInfo = data
                    )
                    userDAO.updateUser(user.id,userProfile)
                }

                else -> throw IllegalArgumentException("Unsupported data type")
            }
        } else {
            //here no user is found and update is cancelled
            false
        }
    }

    override suspend fun logout(user: User, token: String): Boolean {
        revokedTokens.add(token)
        return true
    }



    override suspend fun getStudents(): List<User> {
        val listOfStudents = userDAO.getAllUsers().filter { it.role.lowercase() == "student" }
        return listOfStudents
    }

    override suspend fun updateProfileImage(fileName: String, username: String): Boolean {
        val userid = userDAO.getUserIdByUsername(username)
        if (userid != null){
            return false
        }
        val user = userDAO.getUserByUserId(userid)
        if(user == null){
            return false
        }

        val recordedImageOnDB = userDAO.recordImage(fileName,userid)
        return recordedImageOnDB
    }

    override suspend fun getImageRecordById(imageId: Int): ImageRecord? {
        return userDAO.getImageRecord(imageId)
    }

    override suspend fun getCurrentUserProfileImageId(userId:Int): Int? {
        return userDAO.getCurrentUserProfileImageId(userId)
    }

    override suspend fun getUserProfile(userId: Int): UserProfile? {
        return userDAO.getUserProfileById(userId)
    }

}

//TODO here revoked token table and load it everytime server starts
//should not load it here.
val revokedTokens = mutableSetOf<String>()