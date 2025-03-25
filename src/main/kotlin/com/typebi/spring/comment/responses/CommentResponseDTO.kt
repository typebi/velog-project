package com.typebi.spring.comment.responses

import com.typebi.spring.user.responses.UserResponseDTO
import com.typebi.spring.comment.model.Comment
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
