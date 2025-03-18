package com.typebi.spring.api.responses

import com.typebi.spring.db.entity.Comment

data class CommentResponseDTO (
    val id: Long,
    val postId: Long,
    val content: String,
    val authorId: Long
)

fun commentResponseDTOFrom(comment: Comment): CommentResponseDTO {
    return CommentResponseDTO(
        comment.id,
        comment.post.id,
        comment.content,
        comment.author.id,
    )
}
