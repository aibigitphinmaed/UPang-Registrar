package com.ite393group5.db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp


object UserTable : IntIdTable("user") {
   val username = varchar("username", 100).uniqueIndex()
    val password = varchar("password", 255)
    val role = varchar("role", 50)
    val salt = varchar("salt", 64)
    val imageId = integer("image_id").nullable()
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
}

object PersonalInformationTable : IntIdTable("personal_information") {
    val userIdRef = reference("user_id", UserTable, onDelete =  ReferenceOption.CASCADE)
    val firstName = varchar("first_name", 100)
    val lastName = varchar("last_name", 100)
    val middleName = varchar("middle_name", 100).nullable()
    val extensionName = varchar("extension_name", 100).nullable()
    val gender = varchar("gender", 50)
    val citizenship = varchar("citizenship", 100)
    val religion = varchar("religion", 100)
    val civilStatus = varchar("civil_status", 50)
    val email = varchar("email", 255).uniqueIndex()
    val contactNumber = varchar("contact_number", 15).nullable()
    val birthDate = date("birth_date").nullable()
    val fatherName = varchar("father_name", 255)
    val motherName = varchar("mother_name", 255)
    val spouseName = varchar("spouse_name", 255).nullable()
    val contactPersonNumber = varchar("contact_person_number", 15)
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
}

object LocationInformationTable: IntIdTable("location_information") {
    val userIdRef = reference("user_id", UserTable, onDelete = ReferenceOption.CASCADE)
    val houseNumber = varchar("house_number", 10).nullable()
    val street = varchar("street", 255)
    val zone = varchar("zone", 50).nullable()
    val barangay = varchar("barangay", 100)
    val cityMunicipality = varchar("city_municipality", 100)
    val province = varchar("province", 100)
    val country = varchar("country", 100)
    val postalCode = varchar("postal_code", 10)
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
}

object ImageRecordsTable : IntIdTable("image_records") {
    val fileName = varchar("file_name", 255)
    val fileType = varchar("file_type", 20)
    val userRefId = reference("user_id", UserTable, ReferenceOption.CASCADE)
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
}


object AppointmentTable : IntIdTable("appointment") {
    val studentId = reference("student_id", UserTable, onDelete = ReferenceOption.CASCADE)
    val staffId = reference("staff_id", UserTable, onDelete = ReferenceOption.SET_NULL).nullable()
    val appointmentType = varchar("appointment_type", 255)
    val documentType = varchar("document_type", 255).nullable()
    val reason = text("reason").nullable()
    val requestedDate = date("requested_date")
    val scheduledDate = date("scheduled_date").nullable()
    val status = varchar("status", 50).default("pending")
    val notifiedAt = date("notified_at").nullable()
    val isUrgent = bool("is_urgent").default(false)
    val remarks = text("remarks").nullable()
    val cancellationReason = text("cancellation_reason").nullable()
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
}

object DocumentRecordsTable : IntIdTable("document_records") {
    val studentId = reference("student_id", UserTable, onDelete = ReferenceOption.CASCADE)
    val documentType = varchar("document_type", 255)
    val requestedDate = date("requested_date")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
}

object DocumentRequirementsImagesTable : IntIdTable("document_requirement_image") {
    val documentId = reference("document_id", DocumentRecordsTable, onDelete = ReferenceOption.CASCADE)
    val fileName = varchar("file_name", 255)
    val fileType = varchar("file_type", 20)
    val requestedDate = date("requested_date")
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
}

object AnnouncementTable : IntIdTable("announcements") {
    val announcementTitle = varchar("announcement_title", 255)
    val announcementMessage = varchar("announcement_message", 255)
    val createdAt = timestamp("created_at").defaultExpression(CurrentTimestamp)
    val updatedAt = timestamp("updated_at").defaultExpression(CurrentTimestamp)
}


