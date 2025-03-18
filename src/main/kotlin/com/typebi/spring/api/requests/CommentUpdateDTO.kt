package com.typebi.spring.api.requests

data class CommentUpdateDTO (
    val postId: Long?,
    val content: String?,
    val authorId: Long?
)
