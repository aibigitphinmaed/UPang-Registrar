package com.ite393group5.utilities



@Suppress("SqlNoDataSourceInspection", "SqlResolve")
object QueryStatements {
    const val INSERT_USER = """INSERT INTO "User" (username, password, role, salt,created_at, updated_at) VALUES (?, ?, ?, ?, NOW(), NOW())"""
    const val INSERT_LOCATION =
        """INSERT INTO "LocationInformation" (user_id, house_number, street, zone, barangay, city_municipality, province, country, postal_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"""
    const val INSERT_PERSONAL_INFO ="""INSERT INTO "PersonalInformation" (user_id, first_name, last_name, middle_name, extension_name, gender, citizenship, religion, civil_status, email, contact_number, birth_date, father_name, mother_name, spouse_name, contact_person_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"""

    const val UPDATE_USERNAME = """UPDATE "User" SET username = ? WHERE user_id = ?"""
    const val DELETE_USER = """DELETE FROM "User" WHERE user_id = ?"""
    const val DELETE_LOCATION = """DELETE FROM "LocationInformation" WHERE user_id = ?"""
    const val DELETE_PERSONAL_INFO = """DELETE FROM "PersonalInformation" WHERE user_id = ?"""
    const val FIND_USER = """SELECT user_id, email, password FROM "User" WHERE user_id = ?"""
    const val FIND_USER_BY_CREDENTIALS = """SELECT user_id, username, password FROM "User" WHERE username = ? AND password = ?"""
    const val UPDATE_PASSWORD = """UPDATE "User" SET password = ? WHERE user_id = ?"""
    const val UPDATE_LOCATION =
        """UPDATE "LocationInformation" SET house_number = ?, street = ?, zone = ?, barangay = ?, city_municipality = ?, province = ?, country = ?, postal_code = ? WHERE user_id = ?"""
    const val UPDATE_PERSONAL_INFO =
        """UPDATE "PersonalInformation" SET first_name = ?, last_name = ?, middle_name = ?, extension_name = ?, gender = ?, citizenship = ?, religion = ?, civil_status = ?, email = ?, contact_number = ?, birth_date = ?, father_name = ?, mother_name = ?, spouse_name = ?, contact_person_number = ? WHERE user_id = ?"""
const val FIND_BY_USERNAME = """
    SELECT user_id, username, password, role, salt FROM "User" WHERE username = ?
"""
    const val RETRIEVE_PERSONAL_INFO = """
    SELECT 
        first_name, 
        last_name, 
        middle_name, 
        extension_name, 
        gender, 
        citizenship, 
        religion, 
        civil_status, 
        email, 
        contact_number, 
        birth_date, 
        father_name, 
        mother_name, 
        spouse_name, 
        contact_person_number
    FROM "PersonalInformation"
    WHERE user_id = ?;
"""
    const val RETRIEVE_LOCATION_INFO = """
    SELECT 
        house_number, 
        street, 
        zone, 
        barangay, 
        city_municipality, 
        province, 
        country, 
        postal_code
    FROM "LocationInformation"
    WHERE user_id = ?;
"""
    const val RETRIEVE_ALL_STUDENTS = """
    SELECT user_id, username, password, role, '' AS salt 
    FROM "User" 
    WHERE role = 'student'
"""


}

