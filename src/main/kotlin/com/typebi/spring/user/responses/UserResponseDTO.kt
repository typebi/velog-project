package com.typebi.spring.user.responses

import com.typebi.spring.user.model.User
import org.springframework.hateoas.RepresentationModel


data class UserResponseDTO(
    val id: Long,
    val username: String,
    val email: String,
): RepresentationModel<UserResponseDTO>()

fun userResponseDTOFrom(user: User): UserResponseDTO {
    return UserResponseDTO(
        user.id,
        user.username,
        user.email,
    )
}
