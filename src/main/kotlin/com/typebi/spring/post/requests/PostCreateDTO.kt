package com.typebi.spring.post.requests

data class PostCreateDTO (
    val title: String,
    val content: String,
    val authorId: Long
)