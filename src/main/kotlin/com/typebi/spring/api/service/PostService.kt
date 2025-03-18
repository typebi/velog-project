package com.typebi.spring.api.service

import com.typebi.spring.api.requests.PostCreateDTO
import com.typebi.spring.api.requests.PostUpdateDTO
import com.typebi.spring.api.responses.PostResponseDTO
import org.springframework.stereotype.Service

@Service
interface PostService {
    fun createPost(postCreateDTO: PostCreateDTO): PostResponseDTO
    fun getPosts(): List<PostResponseDTO>
    fun getPostById(id: Long): PostResponseDTO
    fun updatePostById(id: Long, postUpdateDTO: PostUpdateDTO): PostResponseDTO
    fun deletePostById(id: Long): Boolean

}
