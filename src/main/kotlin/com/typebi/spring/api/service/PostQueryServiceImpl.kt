package com.typebi.spring.api.service

import com.typebi.spring.api.responses.CommentResponseDTO
import com.typebi.spring.api.responses.PostResponseDTO
import com.typebi.spring.api.responses.commentResponseDTOFrom
import com.typebi.spring.api.responses.postResponseDTOFrom
import com.typebi.spring.common.exception.NotFoundException
import com.typebi.spring.db.repository.CommentRepository
import com.typebi.spring.db.repository.PostRepository
import com.typebi.spring.db.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class PostQueryServiceImpl(
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository
) : PostQueryService {

    override fun getPosts(): List<PostResponseDTO> {
        return postRepository.findAll().map { postResponseDTOFrom(it) }
    }

    override fun getPostById(id: Long): PostResponseDTO {
        val post = postRepository.findById(id).orElseThrow { NotFoundException("Post not found with id : $id") }
        return postResponseDTOFrom(post)
    }

    override fun getCommentsByPostId(id: Long): List<CommentResponseDTO> {
        postRepository.findById(id).orElseThrow { NotFoundException("Post not found with id : $id") }
        val comments = commentRepository.findByPostId(id)
        return comments.map { commentResponseDTOFrom(it) }
    }
}