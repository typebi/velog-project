package com.typebi.spring.api.requests

data class UserUpdateDTO (
    val username: String?,
    val password: String?,
    val email: String?
)