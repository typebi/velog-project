package com.typebi.spring.user.requests

data class LoginRequestDTO(
    val email: String,
    val password: String
)