package com.typebi.spring.api.responses

import com.typebi.spring.db.entity.Comment
import org.springframework.hateoas.RepresentationModel

data class CommentResponseDTO (
    val id: Long,
    val postId: Long,
    val content: String,
    val authorId: Long
): RepresentationModel<UserResponseDTO>()

fun commentResponseDTOFrom(comment: Comment): CommentResponseDTO {
    return CommentResponseDTO(
        comment.id,
        comment.post.id,
        comment.content,
        comment.author.id,
    )
}
