package com.typebi.spring.api.responses

import com.typebi.spring.db.entity.Post
import org.springframework.hateoas.RepresentationModel

data class PostResponseDTO (
    val id: Long,
    val title: String,
    val content: String,
    val authorId: Long
): RepresentationModel<UserResponseDTO>()

fun postResponseDTOFrom(post: Post): PostResponseDTO {
    return PostResponseDTO(
        post.id,
        post.title,
        post.content,
        post.author.id,
    )
}
