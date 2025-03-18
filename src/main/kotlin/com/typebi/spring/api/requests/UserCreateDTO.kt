package com.typebi.spring.api.requests

data class UserCreateDTO(
    val username: String,
    val password: String,
    val email: String
)
