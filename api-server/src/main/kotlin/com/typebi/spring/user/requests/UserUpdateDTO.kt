package com.typebi.spring.user.requests

data class UserUpdateDTO (
    val username: String?,
    val password: String?,
    val email: String?
)