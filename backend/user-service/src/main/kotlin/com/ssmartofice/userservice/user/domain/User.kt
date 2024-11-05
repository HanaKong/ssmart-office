package com.ssmartofice.userservice.user.domain

import jakarta.validation.constraints.NotBlank
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime
import kotlin.random.Random

class User (
    val id: Int = 0,
    @field:NotBlank(message = "이메일을 입력해주세요.")
    val email: String,
    @field:NotBlank(message = "비밀번호를 입력해주세요.")
    var password: String,
    @field:NotBlank(message = "이름을 입력해주세요.")
    val name: String,
    @field:NotBlank(message = "직급을 입력해주세요.")
    val position: String,
    @field:NotBlank(message = "직무를 입력해주세요.")
    val duty: String,
    @field:NotBlank(message = "이미지를 입력해주세요.")
    val profileImageUrl: String,
    var role: Role = Role.USER,
    val employeeNumber: String = generateEmployeeNumber(),
    val point: Int = 0,
    val status: UserStatus = UserStatus.OFF_DUTY,
    val deleted: Boolean = false,
    @CreatedDate
    val createdDateTime: LocalDateTime? = null,
    @LastModifiedDate
    val updatedDateTime: LocalDateTime? = null
) {
    fun encodePassword(encoder: PasswordEncoder) {
        this.password = encoder.encode(password)
    }

    companion object { //TODO: 클래스 분리하고 중복 체크
        private fun generateEmployeeNumber(): String {
            val randomNumber = Random.nextInt(1000000, 9999999)
            return "S$randomNumber"
        }
    }
}
