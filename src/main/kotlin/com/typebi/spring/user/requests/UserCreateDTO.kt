package com.typebi.spring.user.requests

data class UserCreateDTO(
    val username: String,
    val password: String,
    val email: String
)