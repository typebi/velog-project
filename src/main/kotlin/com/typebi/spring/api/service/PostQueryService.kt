package com.typebi.spring.api.service

import com.typebi.spring.api.responses.CommentResponseDTO
import com.typebi.spring.api.responses.PostResponseDTO
import org.springframework.stereotype.Service

@Service
interface PostQueryService {
    fun getPosts(): List<PostResponseDTO>
    fun getPostById(id: Long): PostResponseDTO
    fun getCommentsByPostId(id: Long): List<CommentResponseDTO>

}
