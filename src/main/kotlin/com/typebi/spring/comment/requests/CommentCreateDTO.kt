package com.typebi.spring.comment.requests

data class CommentCreateDTO (
    val postId: Long,
    val content: String,
    val authorId: Long
)
