package com.typebi.spring.api.requests

data class PostUpdateDTO (
    val title: String?,
    val content: String?,
    val authorId: Long?
)
