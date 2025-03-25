package com.typebi.spring.api.service

import com.typebi.spring.api.requests.PostCreateDTO
import com.typebi.spring.api.requests.PostUpdateDTO
import com.typebi.spring.api.responses.CommentResponseDTO
import com.typebi.spring.api.responses.PostResponseDTO
import org.springframework.stereotype.Service

@Service
interface PostCommandService {
    fun createPost(postCreateDTO: PostCreateDTO): PostResponseDTO
    fun updatePostById(id: Long, postUpdateDTO: PostUpdateDTO): PostResponseDTO
    fun deletePostById(id: Long): Boolean

}
