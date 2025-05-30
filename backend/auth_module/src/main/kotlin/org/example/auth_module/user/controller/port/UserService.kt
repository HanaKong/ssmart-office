package org.example.auth_module.user.controller.port

import org.example.auth_module.user.domain.User

interface UserService {
    fun getUserByEmail(email: String?): User?
    fun addUser(user: User): User
}
