package com.ite393group5.services

import com.ite393group5.dao.UserDAOImpl
import com.ite393group5.models.LocationInfo
import com.ite393group5.models.PersonalInfo
import com.ite393group5.models.Updatable
import com.ite393group5.models.User
import java.sql.Connection

class UserServiceImpl(private val dbConnection: Connection) : UserService {

    val userDAO=UserDAOImpl(dbConnection)
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
       return userDAO.createUser(user,locationInfo,personalInfo)
    }
    //Update function for LocationInfo and PersonalInfo
    //Some can be added
    override suspend fun <T : Updatable> update(data: T,user:User): Boolean {
        val foundUser = userDAO.findUser(user)
        return if (foundUser != null) {
            when(data){
                is LocationInfo -> {
                    userDAO.updateLocation(foundUser, data)
                }

                is PersonalInfo -> {
                    userDAO.updatePersonalInfo(foundUser,data)
                }

                else -> throw IllegalArgumentException("Unsupported data type")
            }
        } else {
            //here no user is found and update is cancelled
            false
        }
    }

    //this is blacklisting tokens for additional security layer lang naman wahahahaha, not to be implemented yet

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
}

//TODO here revoked token table and load it everytime server starts
val revokedTokens = mutableSetOf<String>()