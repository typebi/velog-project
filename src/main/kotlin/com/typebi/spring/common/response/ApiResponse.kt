package com.typebi.spring.common.response

data class ApiResponse<T> (
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
)