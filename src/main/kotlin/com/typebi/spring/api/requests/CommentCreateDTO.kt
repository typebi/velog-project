package com.typebi.spring.api.requests

data class CommentCreateDTO (
    val postId: Long,
    val content: String,
    val authorId: Long
)
