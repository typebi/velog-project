package com.typebi.spring.comment.requests

data class CommentUpdateDTO (
    val postId: Long?,
    val content: String?,
    val authorId: Long?
)
