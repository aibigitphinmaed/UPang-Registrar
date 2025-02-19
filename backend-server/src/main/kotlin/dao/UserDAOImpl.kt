package com.ite393group5.dao

import com.ite393group5.dto.ImageRecord
import com.ite393group5.models.LocationInfo
import com.ite393group5.models.PersonalInfo
import com.ite393group5.models.User
import com.ite393group5.utilities.QueryStatements.DELETE_LOCATION
import com.ite393group5.utilities.QueryStatements.DELETE_PERSONAL_INFO
import com.ite393group5.utilities.QueryStatements.DELETE_USER
import com.ite393group5.utilities.QueryStatements.FIND_BY_USERNAME
import com.ite393group5.utilities.QueryStatements.FIND_USER
import com.ite393group5.utilities.QueryStatements.FIND_USER_BY_CREDENTIALS
import com.ite393group5.utilities.QueryStatements.INSERT_IMAGE_RECORD
import com.ite393group5.utilities.QueryStatements.INSERT_LOCATION
import com.ite393group5.utilities.QueryStatements.INSERT_PERSONAL_INFO
import com.ite393group5.utilities.QueryStatements.INSERT_USER
import com.ite393group5.utilities.QueryStatements.RETRIEVE_ALL_STUDENTS
import com.ite393group5.utilities.QueryStatements.RETRIEVE_IMAGE_RECORD_WITH_ID
import com.ite393group5.utilities.QueryStatements.RETRIEVE_LOCATION_INFO
import com.ite393group5.utilities.QueryStatements.RETRIEVE_PERSONAL_INFO
import com.ite393group5.utilities.QueryStatements.UPDATE_LOCATION
import com.ite393group5.utilities.QueryStatements.UPDATE_PASSWORD
import com.ite393group5.utilities.QueryStatements.UPDATE_PERSONAL_INFO
import com.ite393group5.utilities.QueryStatements.UPDATE_USER
import com.ite393group5.utilities.QueryStatements.UPDATE_USERNAME
import com.ite393group5.utilities.QueryStatements.UPDATE_USER_PROFILE_IMAGE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.Date
import java.sql.Statement

class UserDAOImpl(private val dbConnection: Connection) : UserDAO {

    override suspend fun createUser(user: User, locationInfo: LocationInfo?, personalInfo: PersonalInfo?): User = withContext(Dispatchers.IO) {
        dbConnection.autoCommit = false // Start transaction
        try {
            // Insert into User table
            val userStatement = dbConnection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)
            userStatement.setString(1, user.username)
            userStatement.setString(2, user.password)
            userStatement.setString(3, user.role)
            userStatement.setString(4, user.salt)
            userStatement.executeUpdate()

            val generatedKeys = userStatement.generatedKeys
            if (!generatedKeys.next()) throw Exception("Unable to retrieve the ID of the newly inserted user")

            val userId = generatedKeys.getInt(1)

            // Insert into LocationInfo table
            val locationStatement = dbConnection.prepareStatement(INSERT_LOCATION)
            locationStatement.setInt(1, userId)
            locationStatement.setString(2, locationInfo!!.houseNumber)
            locationStatement.setString(3, locationInfo.street)
            locationStatement.setString(4, locationInfo.zone)
            locationStatement.setString(5, locationInfo.barangay)
            locationStatement.setString(6, locationInfo.cityMunicipality)
            locationStatement.setString(7, locationInfo.province)
            locationStatement.setString(8, locationInfo.country)
            locationStatement.setString(9, locationInfo.postalCode)
            locationStatement.executeUpdate()

            // Insert into PersonalInfo table
            val personalStatement = dbConnection.prepareStatement(INSERT_PERSONAL_INFO)
            personalStatement.setInt(1, userId)
            personalStatement.setString(2, personalInfo!!.firstName)
            personalStatement.setString(3, personalInfo.lastName)
            personalStatement.setString(4, personalInfo.middleName)
            personalStatement.setString(5, personalInfo.extensionName)
            personalStatement.setString(6, personalInfo.gender)
            personalStatement.setString(7, personalInfo.citizenship)
            personalStatement.setString(8, personalInfo.religion)
            personalStatement.setString(9, personalInfo.civilStatus)
            personalStatement.setString(10, personalInfo.email)
            personalStatement.setString(11, personalInfo.number)
            personalStatement.setDate(12, Date.valueOf(personalInfo.birthDate))
            personalStatement.setString(13, personalInfo.fatherName)
            personalStatement.setString(14, personalInfo.motherName)
            personalStatement.setString(15, personalInfo.spouseName)
            personalStatement.setString(16, personalInfo.contactPersonNumber)
            personalStatement.executeUpdate()

            dbConnection.commit() // Commit transaction
            return@withContext user.copy(id = userId) // Return user with assigned ID

        } catch (e: Exception) {
            dbConnection.rollback() // Rollback on error
            throw e
        } finally {
            dbConnection.autoCommit = true
        }
    }

    override suspend fun updateUsername(user: User): User = withContext(Dispatchers.IO) {
        val statement = dbConnection.prepareStatement(UPDATE_USERNAME)
        statement.setString(1, user.username)
        statement.setInt(2, user.id!!)
        statement.executeUpdate()
        return@withContext user
    }

    override suspend fun deleteUser(user: User) = withContext(Dispatchers.IO) {
        dbConnection.autoCommit = false
        try {
            val deletePersonalStatement = dbConnection.prepareStatement(DELETE_PERSONAL_INFO)
            deletePersonalStatement.setInt(1, user.id!!)
            deletePersonalStatement.executeUpdate()

            val deleteLocationStatement = dbConnection.prepareStatement(DELETE_LOCATION)
            deleteLocationStatement.setInt(1, user.id)
            deleteLocationStatement.executeUpdate()

            val deleteUserStatement = dbConnection.prepareStatement(DELETE_USER)
            deleteUserStatement.setInt(1, user.id)
            deleteUserStatement.executeUpdate()

            dbConnection.commit()
        } catch (e: Exception) {
            dbConnection.rollback()
            throw e
        } finally {
            dbConnection.autoCommit = true
        }
    }

    override suspend fun findUser(user: User): User? = withContext(Dispatchers.IO) {
        val statement = dbConnection.prepareStatement(FIND_USER)
        statement.setInt(1, user.id!!)
        val resultSet = statement.executeQuery()
        return@withContext if (resultSet.next()) {
            User( resultSet.getInt("id"), resultSet.getString("email"), resultSet.getString("password"),
                resultSet.getString("role"), resultSet.getString("salt"),
                )
        } else null
    }

    override suspend fun findUserByUsernameAndPassword(user:User): User? = withContext(Dispatchers.IO) {
        val statement = dbConnection.prepareStatement(FIND_USER_BY_CREDENTIALS)
        statement.setString(1, user.username)
        statement.setString(2, user.password)
        val resultSet = statement.executeQuery()
        return@withContext if (resultSet.next()) {
            User(
                resultSet.getInt("id"), resultSet.getString("email"), resultSet.getString("password"),
                resultSet.getString("role"), resultSet.getString("salt")
            )
        } else null
    }

    override suspend fun updatePassword(user: User, newPassword: String): Boolean = withContext(Dispatchers.IO) {
        val statement = dbConnection.prepareStatement(UPDATE_PASSWORD)
        statement.setString(1, newPassword)
        statement.setInt(2, user.id!!)
        return@withContext statement.executeUpdate() > 0 // Returns true if at least one row was updated
    }



    override suspend fun updateLocationInfo(user: User, locationInfo: LocationInfo?): Boolean = withContext(Dispatchers.IO) {
        val statement = dbConnection.prepareStatement(UPDATE_LOCATION)
        statement.setString(1, locationInfo!!.houseNumber)
        statement.setString(2, locationInfo.street)
        statement.setString(3, locationInfo.zone)
        statement.setString(4, locationInfo.barangay)
        statement.setString(5, locationInfo.cityMunicipality)
        statement.setString(6, locationInfo.province)
        statement.setString(7, locationInfo.country)
        statement.setString(8, locationInfo.postalCode)
        statement.setInt(9, user.id!!) // Foreign key reference to User
        return@withContext statement.executeUpdate() > 0
    }

    override suspend fun updatePersonalInfo(user: User, personalInfo: PersonalInfo?): Boolean = withContext(Dispatchers.IO) {
        val statement = dbConnection.prepareStatement(UPDATE_PERSONAL_INFO)
        statement.setString(1, personalInfo!!.firstName)
        statement.setString(2, personalInfo.lastName)
        statement.setString(3, personalInfo.middleName)
        statement.setString(4, personalInfo.extensionName)
        statement.setString(5, personalInfo.gender)
        statement.setString(6, personalInfo.citizenship)
        statement.setString(7, personalInfo.religion)
        statement.setString(8, personalInfo.civilStatus)
        statement.setString(9, personalInfo.email)
        statement.setString(10, personalInfo.number)
        statement.setDate(11, Date.valueOf(personalInfo.birthDate))
        statement.setString(12, personalInfo.fatherName)
        statement.setString(13, personalInfo.motherName)
        statement.setString(14, personalInfo.spouseName)
        statement.setString(15, personalInfo.contactPersonNumber)
        statement.setInt(16, user.id!!) // Foreign key reference to User
        return@withContext statement.executeUpdate() > 0
    }

    override suspend fun findByUsername(username: String): User?  = withContext(Dispatchers.IO) {
        val statement = dbConnection.prepareStatement(FIND_BY_USERNAME)
        statement.setString(1, username)
        val resultSet = statement.executeQuery()
        return@withContext if (resultSet.next()) {
            User(
                resultSet.getInt("user_id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("role"),
                resultSet.getString("salt")
            )
        } else null
    }

    override suspend fun retrievePersonalInformation(userId: Int): PersonalInfo? = withContext(Dispatchers.IO) {
        val statement = dbConnection.prepareStatement(RETRIEVE_PERSONAL_INFO)
        statement.setInt(1, userId)
        val resultSet = statement.executeQuery()

        return@withContext if (resultSet.next()) {
            PersonalInfo(
                firstName = resultSet.getString("first_name"),
                lastName = resultSet.getString("last_name"),
                middleName = resultSet.getString("middle_name"),
                extensionName = resultSet.getString("extension_name"),
                gender = resultSet.getString("gender"),
                citizenship = resultSet.getString("citizenship"),
                religion = resultSet.getString("religion"),
                civilStatus = resultSet.getString("civil_status"),
                email = resultSet.getString("email"),
                number = resultSet.getString("contact_number"),
                birthDate = resultSet.getDate("birth_date")?.toLocalDate(),
                fatherName = resultSet.getString("father_name"),
                motherName = resultSet.getString("mother_name"),
                spouseName = resultSet.getString("spouse_name"),
                contactPersonNumber = resultSet.getString("contact_person_number")
            )
        } else {
            null
        }
    }

    override suspend fun retrieveAddressInformation(userId: Int): LocationInfo? = withContext(Dispatchers.IO) {
        val statement = dbConnection.prepareStatement(RETRIEVE_LOCATION_INFO)
        statement.setInt(1, userId)
        val resultSet = statement.executeQuery()

        return@withContext if (resultSet.next()) {
            LocationInfo(
                houseNumber = resultSet.getString("house_number"),
                street = resultSet.getString("street"),
                zone = resultSet.getString("zone"),
                barangay = resultSet.getString("barangay"),
                cityMunicipality = resultSet.getString("city_municipality"),
                province = resultSet.getString("province"),
                country = resultSet.getString("country"),
                postalCode = resultSet.getString("postal_code")
            )
        } else {
            null
        }
    }

    override suspend fun getAllStudents(): List<User> = withContext(Dispatchers.IO) {
        val statement = dbConnection.prepareStatement(RETRIEVE_ALL_STUDENTS)
        val resultSet = statement.executeQuery()
        val students = mutableListOf<User>()
        while (resultSet.next()) {
            val user = User(
                id = resultSet.getInt("user_id"),
                username = resultSet.getString("username"),
                password = resultSet.getString("password"),
                role = resultSet.getString("role"),
                salt = ""
            )
            students.add(user)
        }

        resultSet.close()

        return@withContext students
    }

   override suspend fun updateUser(userid: Int?, data: User): Boolean = withContext(Dispatchers.IO) {

       if(userid != null) {
           dbConnection.prepareStatement(UPDATE_USER).use { statement ->
               statement.setString(1, data.username)
               statement.setString(2, data.password)
               statement.setString(3, data.salt)
               statement.setInt(4, userid)

               val rowsUpdated = statement.executeUpdate()
               return@withContext rowsUpdated > 0
           }
       }else{
           return@withContext false
       }

    }




    override suspend fun recordImage(user: User, fileName: String): Boolean = withContext(Dispatchers.IO) {
        val fileType = fileName.substringAfterLast(".", "unknown")
        dbConnection.prepareStatement(INSERT_IMAGE_RECORD).use { statement ->
            statement.setString(1, fileName)
            statement.setString(2, fileType)
            statement.setInt(3, user.id!!)

            val resultSet = statement.executeQuery()
            resultSet.next()
            val imageId = resultSet.getInt("id")
            if (imageId > 0) {
                println(imageId)
                dbConnection.prepareStatement(UPDATE_USER_PROFILE_IMAGE).use { statement ->
                    statement.setInt(1, imageId)
                    statement.setInt(2, user.id)
                    val rowsUpdated = statement.executeUpdate()
                    return@withContext rowsUpdated > 0
                }
            }

            return@withContext false
        }

        return@withContext false
    }

    override suspend fun getImageRecord(imageId: Int): ImageRecord? = withContext(Dispatchers.IO) {

        dbConnection.prepareStatement(RETRIEVE_IMAGE_RECORD_WITH_ID).use { statement ->
            statement.setInt(1, imageId)
            val resultSet = statement.executeQuery()

            return@withContext if (resultSet.next()) {
                ImageRecord(
                    id = imageId,
                    fileName = resultSet.getString("file_name"),
                    fileType = resultSet.getString("file_type"),
                    userId = resultSet.getInt("user_id"),
                )
            } else null
        }
    }
}

