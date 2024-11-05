package com.ssmartofice.userservice.user.infrastructure

import com.ssmartofice.userservice.user.domain.Role
import com.ssmartofice.userservice.user.domain.User
import com.ssmartofice.userservice.user.domain.UserStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "users")
class UserEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    val id: Int = 0,
    val email: String,
    val password: String,
    val name: String,
    val position: String,
    val duty: String,
    val profileImageUrl: String,
    val role: Role = Role.USER,
    val employeeNumber: String,
    val point: Int = 0,
    val status: UserStatus = UserStatus.OFF_DUTY,
    val deleted: Boolean = false,
) {
    @Column(nullable = false, updatable = false)
    private var createdDateTime: LocalDateTime? = null
        private set

    @Column(nullable = false)
    private var updatedDateTime: LocalDateTime? = null
        private set

    companion object {
        fun fromModel(user: User): UserEntity {
            return UserEntity(
                id = user.id,
                email = user.email,
                password = user.password,
                name = user.name,
                position = user.position,
                duty = user.duty,
                profileImageUrl = user.profileImageUrl,
                role = user.role,
                employeeNumber = user.employeeNumber,
                point = user.point,
                status = user.status,
                deleted = user.deleted
            )
        }
    }

    fun toModel(): User {
        return User(
            id = id,
            email = email,
            password = password,
            name = name,
            position = position,
            duty = duty,
            profileImageUrl = profileImageUrl,
            role = role,
            employeeNumber = employeeNumber,
            point = point,
            status = status,
            deleted = deleted
        )
    }

    @PrePersist
    fun onCreate() {
        val currentTime = LocalDateTime.now()
        createdDateTime = currentTime
        updatedDateTime = currentTime
    }

    @PreUpdate
    fun onUpdate() {
        updatedDateTime = LocalDateTime.now()
    }
}