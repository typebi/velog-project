package com.typebi.spring.post.service

import com.typebi.spring.post.requests.PostCreateDTO
import com.typebi.spring.post.requests.PostUpdateDTO
import com.typebi.spring.post.responses.PostResponseDTO
import org.springframework.stereotype.Service

@Service
interface PostCommandService {
    fun createPost(postCreateDTO: PostCreateDTO): PostResponseDTO
    fun updatePostById(id: Long, postUpdateDTO: PostUpdateDTO): PostResponseDTO
    fun deletePostById(id: Long): Boolean

}