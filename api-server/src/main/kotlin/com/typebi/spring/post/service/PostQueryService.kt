package com.typebi.spring.post.service

import com.typebi.spring.comment.responses.CommentResponseDTO
import com.typebi.spring.common.response.CursorPage
import com.typebi.spring.post.responses.PostResponseDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
interface PostQueryService {
    fun getPosts(pageable: Pageable): Page<PostResponseDTO>
    fun getPostById(id: Long): PostResponseDTO
    fun getCommentsByPostId(id: Long, pageable: Pageable): Page<CommentResponseDTO>
    fun getPostsFeed(cursor: Long?): CursorPage<PostResponseDTO>

}