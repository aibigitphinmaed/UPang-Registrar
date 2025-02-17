package com.ite393group5.services

import com.ite393group5.models.User

object FakeStudentData {
    fun getFakeStudents(): List<User> {
        // Mocking a list of fake students
        return listOf(
            User(
                id = 1,
                username = "student001",
                password = "hashedPassword1",
                role = "student",
                salt = "salt1"
            ),
            User(
                id = 2,
                username = "student002",
                password = "hashedPassword2",
                role = "student",
                salt = "salt2"
            ),
            User(
                id = 3,
                username = "student003",
                password = "hashedPassword3",
                role = "student",
                salt = "salt3"
            ),
            User(
                id = 4,
                username = "student004",
                password = "hashedPassword4",
                role = "student",
                salt = "salt4"
            ),
            User(
                id = 5,
                username = "student005",
                password = "hashedPassword5",
                role = "student",
                salt = "salt5"
            )
        )
    }
}
