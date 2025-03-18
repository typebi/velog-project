package com.typebi.spring.api.responses

import com.typebi.spring.db.entity.User


data class UserResponseDTO(
    val id: Long,
    val username: String,
    val email: String,
)

fun userResponseDTOFrom(user: User): UserResponseDTO {
    return UserResponseDTO(
        user.id,
        user.username,
        user.email,
    )
}
