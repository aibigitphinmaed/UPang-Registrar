package com.ite393group5.services

import com.ite393group5.dao.UserDAOImpl
import com.ite393group5.dto.UserProfile
import com.ite393group5.models.LocationInfo
import com.ite393group5.models.PersonalInfo
import com.ite393group5.models.Updatable
import com.ite393group5.models.User
import java.sql.Connection

class UserServiceImpl(private val dbConnection: Connection) : UserService {

    val userDAO = UserDAOImpl(dbConnection)
    override suspend fun findByUsername(username: String): User? {
        return userDAO.findByUsername(username)
    }

    override suspend fun findByUsernameAndPassword(user: User): User? {
        return userDAO.findUserByUsernameAndPassword(user)
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
    override suspend fun <T : Updatable> update(data: T, user: User): Boolean {
        val foundUser = userDAO.findUser(user)
        return if (foundUser != null) {
            when (data) {
                is LocationInfo -> {
                    userDAO.updateLocationInfo(foundUser, data)
                }

                is PersonalInfo -> {
                    userDAO.updatePersonalInfo(foundUser, data)
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

    override suspend fun retrieveProfileById(userid: Int): PersonalInfo? {
        return userDAO.retrievePersonalInformation(userid)
    }

    override suspend fun retrieveAddressById(userid: Int): LocationInfo? {
        return userDAO.retrieveAddressInformation(userid)
    }

    override suspend fun <T : Updatable> updateProfile(
        data: T,
        username: String
    ): Boolean {
        val foundUser = userDAO.findByUsername(username)
        return if (foundUser != null) {
            when (data) {
                is UserProfile -> {
                    userDAO.updatePersonalInfo(foundUser, data.userPersonalInfo)
                    userDAO.updateLocationInfo(foundUser, data.userAddressInfo)
                }
                is User -> {
                    userDAO.updateUser(foundUser.id, data)
                }
                else -> throw IllegalArgumentException("Unsupported data type")
            }
        } else {
            //here no user is found and update is cancelled
            throw IllegalArgumentException("No user with username $username")
            false
        }

    }

    override suspend fun registerStudent(
        user: User,
        userProfile: UserProfile
    ): User {
        val createdUser = userDAO.createUser(user =user, locationInfo = userProfile.userAddressInfo, personalInfo = userProfile.userPersonalInfo)
        return createdUser

    }

    override suspend fun getStudents(): List<User> {
        val listOfStudents = userDAO.getAllStudents()
        return listOfStudents
    }
}

//TODO here revoked token table and load it everytime server starts
//should not load it here.
val revokedTokens = mutableSetOf<String>()