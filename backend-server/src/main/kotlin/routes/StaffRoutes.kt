package com.ite393group5.routes

import com.ite393group5.models.LocationInfo
import com.ite393group5.models.PersonalInfo
import com.ite393group5.models.TokenConfig
import com.ite393group5.models.User
import com.ite393group5.services.UserService
import com.ite393group5.utilities.JwtTokenService
import com.ite393group5.utilities.SHA256HashingService
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import java.time.LocalDate


fun Route.staffRoutes(
    userServiceImpl: UserService,
    tokenService: JwtTokenService,
    studentTokenConfig: TokenConfig,
    staffTokenConfig: TokenConfig
) {
    authenticate("staff-auth") {
        get("hello-staff"){
            call.respondText("Hello Staff!")
        }
    }

    //delete this later on
    post("/createUsersForTesting") {
        try {
            val studentSaltedHash = SHA256HashingService().generateSaltedHash("student123")
            val studentUser = User(
                id = null,
                username = "student123",
                password = studentSaltedHash.hash,
                role = "student",
                salt = studentSaltedHash.salt,
            )

            val staffSaltedHash = SHA256HashingService().generateSaltedHash("staff123")

            val staffUser = User(
                id = null,
                username = "staff123",
                password = staffSaltedHash.hash,
                role = "staff",
                salt = staffSaltedHash.salt,
            )

            val studentLocation = LocationInfo(
                houseNumber = "123",
                street = "Student St.",
                zone = "1",
                barangay = "Student Barangay",
                cityMunicipality = "Student City",
                province = "Student Province",
                country = "PH",
                postalCode = "1234"
            )

            val staffLocation = LocationInfo(
                houseNumber = "456",
                street = "Staff St.",
                zone = "2",
                barangay = "Staff Barangay",
                cityMunicipality = "Staff City",
                province = "Staff Province",
                country = "PH",
                postalCode = "5678"
            )

            val studentPersonalInfo = PersonalInfo(
                firstName = "John",
                lastName = "Doe",
                middleName = "S",
                extensionName = "",
                gender = "Male",
                citizenship = "Filipino",
                religion = "Catholic",
                civilStatus = "Single",
                email = "student123@email.com",
                number = "09123456789",
                birthDate = LocalDate.of(2002, 5, 10),
                fatherName = "Father Doe",
                motherName = "Mother Doe",
                spouseName = null,
                contactPersonNumber = "09129876543"
            )

            val staffPersonalInfo = PersonalInfo(
                firstName = "Jane",
                lastName = "Smith",
                middleName = "M",
                extensionName = "",
                gender = "Female",
                citizenship = "Filipino",
                religion = "Catholic",
                civilStatus = "Married",
                email = "staff123@email.com",
                number = "09234567890",
                birthDate = LocalDate.of(1990, 8, 20),
                fatherName = "Father Smith",
                motherName = "Mother Smith",
                spouseName = "Mr. Smith",
                contactPersonNumber = "09345678901"
            )

            // Register users
            val createdStudent = userServiceImpl.register(studentUser, studentLocation, studentPersonalInfo)
            val createdStaff = userServiceImpl.register(staffUser, staffLocation, staffPersonalInfo)

            call.respond(HttpStatusCode.Created, mapOf("student" to createdStudent, "staff" to createdStaff))
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, "Error creating users: ${e.localizedMessage}")
        }
    }

}