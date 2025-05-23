package org.example.auth_module.user.service.port

import org.example.auth_module.user.controller.port.UserService
import org.example.auth_module.user.domain.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    val passwordEncoder: BCryptPasswordEncoder,
    val userRepository: UserRepository
) : UserService{

    override fun getUserByEmail(email: String?): User? {
        return userRepository.findByEmail(email)
    }

    override fun addUser(user: User): User {
        user.encodePassword(passwordEncoder)
        return userRepository.save(user)
    }
}