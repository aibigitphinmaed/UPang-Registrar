package com.ite393group5.db

import com.ite393group5.models.User
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UserTable)

    var username by UserTable.username
    var password by UserTable.password
    var role by UserTable.role
    var salt by UserTable.salt
    var imageId by UserTable.imageId

    fun toUser(): User {
        return User(
            id = id.value,
            username = username,
            password = password,
            role = role,
            salt = salt,
            imageId = imageId
        )
    }
}