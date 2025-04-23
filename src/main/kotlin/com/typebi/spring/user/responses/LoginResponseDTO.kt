package com.typebi.spring.user.responses

data class LoginResponseDTO(
    val token: String,
    val publicId: String
)
