package com.ite393group5.dao

import com.ite393group5.db.ImageRecordsTable
import com.ite393group5.db.LocationInformationTable
import com.ite393group5.db.PersonalInformationTable
import com.ite393group5.db.UserTable
import com.ite393group5.dto.ImageRecord
import com.ite393group5.dto.user.UserProfile
import com.ite393group5.models.LocationInfo
import com.ite393group5.models.PersonalInfo
import com.ite393group5.models.User
import kotlinx.datetime.Clock.System
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction


class UserDAOImpl : UserDAO {

    override fun createUser(user: User, locationInfo: LocationInfo?, personalInfo: PersonalInfo?): User {
        return transaction {
            val userId = UserTable.insertAndGetId {
                it[username] = user.username
                it[password] = user.password
                it[role] = user.role
                it[salt] = user.salt
            }.value

            locationInfo?.let {
                LocationInformationTable.insert {
                    it[userIdRef] = userId
                    it[houseNumber] = locationInfo.houseNumber ?: ""
                    it[street] = locationInfo.street ?: ""
                    it[zone] = locationInfo.zone ?: ""
                    it[barangay] = locationInfo.barangay ?: ""
                    it[cityMunicipality] = locationInfo.cityMunicipality ?: ""
                    it[province] = locationInfo.province ?: ""
                    it[country] = locationInfo.country ?: ""
                    it[postalCode] = locationInfo.postalCode ?: ""
                }
            }

            personalInfo?.let {
                PersonalInformationTable.insert {
                    it[userIdRef] = userId
                    it[firstName] = personalInfo.firstName ?: ""
                    it[lastName] = personalInfo.lastName ?: ""
                    it[middleName] = personalInfo.middleName ?: ""
                    it[extensionName] = personalInfo.extensionName ?: ""
                    it[gender] = personalInfo.gender ?: ""
                    it[citizenship] = personalInfo.citizenship ?: ""
                    it[religion] = personalInfo.religion ?: ""
                    it[civilStatus] = personalInfo.civilStatus ?: ""
                    it[email] = personalInfo.email ?: ""
                    it[contactNumber] = personalInfo.number
                    it[birthDate] = personalInfo.birthDate?.toKotlinLocalDate()
                    it[fatherName] = personalInfo.fatherName ?: ""
                    it[motherName] = personalInfo.motherName ?: ""
                    it[spouseName] = personalInfo.spouseName ?: ""
                    it[contactPersonNumber] = personalInfo.contactPersonNumber ?: ""
                }
            }

            user.copy(id = userId)
        }
    }


    override fun getUserProfileById(userId: Int): UserProfile? {
        return transaction {
            // Fetch User from UserTable
            val userRow = UserTable.select ( UserTable.id ).where{ UserTable.id eq userId }.singleOrNull() ?: return@transaction null

            // Fetch Personal Information (if exists)
            val personalInfoRow = PersonalInformationTable.selectAll().where{ PersonalInformationTable.userIdRef eq userId }.singleOrNull()

            // Fetch Location Information (if exists)
            val locationInfoRow = LocationInformationTable.selectAll().where{ LocationInformationTable.userIdRef eq userId }.singleOrNull()

            // Map Personal Information (if available)
            val personalInfo = personalInfoRow?.let {
                PersonalInfo(
                    firstName = it[PersonalInformationTable.firstName],
                    lastName = it[PersonalInformationTable.lastName],
                    middleName = it[PersonalInformationTable.middleName].toString(),
                    extensionName = it[PersonalInformationTable.extensionName],
                    gender = it[PersonalInformationTable.gender],
                    citizenship = it[PersonalInformationTable.citizenship],
                    religion = it[PersonalInformationTable.religion],
                    civilStatus = it[PersonalInformationTable.civilStatus],
                    email = it[PersonalInformationTable.email],
                    number = it[PersonalInformationTable.contactNumber].toString(),
                    birthDate = it[PersonalInformationTable.birthDate]?.toJavaLocalDate(),
                    fatherName = it[PersonalInformationTable.fatherName],
                    motherName = it[PersonalInformationTable.motherName],
                    spouseName = it[PersonalInformationTable.spouseName],
                    contactPersonNumber = it[PersonalInformationTable.contactPersonNumber]
                )
            }

            // Map Location Information (if available)
            val locationInfo = locationInfoRow?.let {
                LocationInfo(
                    houseNumber = it[LocationInformationTable.houseNumber].toString(),
                    street = it[LocationInformationTable.street],
                    zone = it[LocationInformationTable.zone].toString(),
                    barangay = it[LocationInformationTable.barangay],
                    cityMunicipality = it[LocationInformationTable.cityMunicipality],
                    province = it[LocationInformationTable.province],
                    country = it[LocationInformationTable.country],
                    postalCode = it[LocationInformationTable.postalCode]
                )
            }

            return@transaction UserProfile(
                userPersonalInfo = personalInfo,
                userAddressInfo = locationInfo
            )
        }
    }


    override fun updateUser(userId: Int?, updatedUser: UserProfile?): Boolean {
        return transaction {
            val userExists = UserTable.selectAll().where{UserTable.id eq userId}.count() > 0
            if(!userExists)return@transaction false
            if(updatedUser == null){
                return@transaction false
            }

            // 1️⃣ Update User table (if data is provided)
            updatedUser.userAccount?.let {userAccount ->
                UserTable.update({ UserTable.id eq userId }) {
                    it[username] = userAccount.username
                    it[password] = userAccount.password
                    it[role] = userAccount.role
                    it[salt] = userAccount.salt
                    it[imageId] = userAccount.imageId
                    it[updatedAt] = System.now()
                }
            }

            // 2️⃣ Update PersonalInformation table (if data is provided)
            updatedUser.userPersonalInfo?.let { personalInfo ->
                PersonalInformationTable.update({ PersonalInformationTable.userIdRef eq userId }) {
                    it[firstName] = personalInfo.firstName ?: ""
                    it[lastName] = personalInfo.lastName ?: ""
                    it[middleName] = personalInfo.middleName ?: ""
                    it[extensionName] = personalInfo.extensionName ?: ""
                    it[gender] = personalInfo.gender ?: ""
                    it[citizenship] = personalInfo.citizenship ?: ""
                    it[religion] = personalInfo.religion ?: ""
                    it[civilStatus] = personalInfo.civilStatus ?: ""
                    it[email] = personalInfo.email ?: ""
                    it[contactNumber] = personalInfo.number ?: ""
                    it[birthDate] = personalInfo.birthDate?.toKotlinLocalDate()
                    it[fatherName] = personalInfo.fatherName ?: ""
                    it[motherName] = personalInfo.motherName ?: ""
                    it[spouseName] = personalInfo.spouseName ?: ""
                    it[contactPersonNumber] = personalInfo.contactPersonNumber ?: ""
                    it[updatedAt] = System.now()
                }
            }

            // 3️⃣ Update LocationInformation table (if data is provided)
            updatedUser.userAddressInfo?.let { locationInfo ->
                LocationInformationTable.update( { LocationInformationTable.userIdRef eq userId }){
                    it[houseNumber] = locationInfo.houseNumber ?: ""
                    it[street] = locationInfo.street ?: ""
                    it[zone] = locationInfo.zone ?: ""
                    it[barangay] = locationInfo.barangay ?: ""
                    it[cityMunicipality] = locationInfo.cityMunicipality ?: ""
                    it[province] = locationInfo.province ?: ""
                    it[country] = locationInfo.country ?: ""
                    it[postalCode] = locationInfo.postalCode ?: ""
                    it[updatedAt] = System.now()
                }
            }

            return@transaction true
        }


    }

    override fun deleteUser(userId: Int): Boolean {
        return transaction {
            // Check if user exists
            val userExists = UserTable.select ( UserTable.id eq userId ).singleOrNull() != null
            if (!userExists) return@transaction false

            // Delete related records first due to foreign key constraints (if applicable)
            PersonalInformationTable.deleteWhere { PersonalInformationTable.userIdRef eq userId }
            LocationInformationTable.deleteWhere { LocationInformationTable.userIdRef eq userId }

            // Delete the user
            val deletedRows = UserTable.deleteWhere { UserTable.id eq userId }

            // Return true if a user was deleted, false otherwise
            return@transaction deletedRows > 0
        }
    }

    override fun getUserIdByUsername(username: String): Int? {
        return transaction {
            println("Looking for: $username")
            UserTable
                .select ( UserTable.id ).where { UserTable.username eq username }
                .singleOrNull()
                ?.get(UserTable.id)
                ?.value
        }
    }

    override fun getUserByUserId(userId: Int?): User? {
        return transaction {
            UserTable
                .selectAll().where { UserTable.id eq userId }
                .mapNotNull { row ->
                    User(
                        id = userId,
                        username = row[UserTable.username],
                        password = row[UserTable.password],
                        role = row[UserTable.role],
                        salt = row[UserTable.salt],
                        imageId = row[UserTable.imageId],
                    )
                }
                .singleOrNull()
        }
    }

    override fun getAllUsers(): List<User> {
        return transaction {
            UserTable
                .selectAll()
                .map { row ->
                    User(
                        id = row[UserTable.id].value,
                        username = row[UserTable.username],
                        password = row[UserTable.password],
                        role = row[UserTable.role],
                        salt = row[UserTable.salt],
                        imageId = row[UserTable.imageId],
                    )
                }
        }
    }

    override fun recordImage(fileName: String, userId: Int?): Int {
        return transaction {
            try {
                if(userId == null){
                    return@transaction -1
                }
               val imageId = ImageRecordsTable.insert { row ->
                   row[ImageRecordsTable.fileName] = fileName
                   row[fileType] = getFileType(fileName)
                   row[userRefId] = userId
                   row[createdAt] = System.now()
               }[ImageRecordsTable.id].value

                return@transaction imageId // Return true if insertion succeeds
            } catch (e: Exception) {
                e.printStackTrace()
                return@transaction -1 // Return false if an error occurs
            }
        }
    }
    private fun getFileType(fileName: String): String {
        return fileName.substringAfterLast('.', "").lowercase()
    }


    override fun getImageRecord(imageId: Int): ImageRecord? {
        return transaction {
            ImageRecordsTable.selectAll().where { ImageRecordsTable.id eq imageId }
                .mapNotNull { row ->
                    ImageRecord(
                        id = row[ImageRecordsTable.id].value, // Extract ID from EntityID
                        fileName = row[ImageRecordsTable.fileName],
                        fileType = row[ImageRecordsTable.fileType],
                        userId = row[ImageRecordsTable.userRefId].value,
                    )
                }
                .singleOrNull()
        }
    }

    override fun getCurrentUserProfileImageId(userId: Int): Int? {
        return transaction {
            return@transaction UserTable
                .select( UserTable.imageId)
                .where { UserTable.id eq userId }
                .singleOrNull()
                ?.get(UserTable.imageId)
        }
    }



}

