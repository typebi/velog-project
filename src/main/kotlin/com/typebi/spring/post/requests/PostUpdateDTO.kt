package com.typebi.spring.post.requests

data class PostUpdateDTO (
    val title: String?,
    val content: String?,
    val authorId: Long?
)