package com.typebi.spring.api.requests

data class PostCreateDTO (
    val title: String,
    val content: String,
    val authorId: Long
)
