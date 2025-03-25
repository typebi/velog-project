package com.typebi.spring.post.service

import com.typebi.spring.comment.responses.CommentResponseDTO
import com.typebi.spring.post.responses.PostResponseDTO
import org.springframework.stereotype.Service

@Service
interface PostQueryService {
    fun getPosts(): List<PostResponseDTO>
    fun getPostById(id: Long): PostResponseDTO
    fun getCommentsByPostId(id: Long): List<CommentResponseDTO>

}