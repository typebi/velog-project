package com.typebi.spring.api.service

import com.typebi.spring.api.requests.PostCreateDTO
import com.typebi.spring.api.requests.PostUpdateDTO
import com.typebi.spring.api.responses.PostResponseDTO
import com.typebi.spring.api.responses.postResponseDTOFrom
import com.typebi.spring.common.exception.NotFoundException
import com.typebi.spring.db.entity.postOf
import com.typebi.spring.db.repository.PostRepository
import com.typebi.spring.db.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PostServiceImpl(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) : PostService {

    @Transactional
    override fun createPost(postCreateDTO: PostCreateDTO): PostResponseDTO {
        val author = userRepository.findById(postCreateDTO.authorId).orElseThrow { NotFoundException("User not found with id : ${postCreateDTO.authorId}") }
        return postResponseDTOFrom(postRepository.save(postOf(postCreateDTO, author)))
    }

    override fun getPosts(): List<PostResponseDTO> {
        return postRepository.findAll().map { postResponseDTOFrom(it) }
    }

    override fun getPostById(id: Long): PostResponseDTO {
        val post = postRepository.findById(id).orElseThrow { NotFoundException("Post not found with id : $id")}
        return postResponseDTOFrom(post)
    }

    @Transactional
    override fun updatePostById(id: Long, postUpdateDTO: PostUpdateDTO): PostResponseDTO {
        val post = postRepository.findById(id).orElseThrow { NotFoundException("Post not found with id : $id")}

        postUpdateDTO.title?.let { post.title = it }
        postUpdateDTO.content?.let { post.content = it }
        postUpdateDTO.authorId?.let {
            val author = userRepository.findById(it).orElseThrow { NotFoundException("User not found with id : ${it}") }
            post.author = author
        }

        return postResponseDTOFrom(post)
    }

    @Transactional
    override fun deletePostById(id: Long): Boolean {
        val post = postRepository.findById(id).orElseThrow { NotFoundException("Post not found with id : $id")}
        postRepository.delete(post)
        return true
    }
}