package com.typebi.spring.post.service

import com.typebi.spring.comment.responses.CommentResponseDTO
import com.typebi.spring.post.responses.PostResponseDTO
import com.typebi.spring.comment.responses.commentResponseDTOFrom
import com.typebi.spring.post.responses.postResponseDTOFrom
import com.typebi.spring.common.exception.NotFoundException
import com.typebi.spring.comment.repository.CommentRepository
import com.typebi.spring.post.repository.PostRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostQueryServiceImpl(
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository
) : PostQueryService {

    @Transactional(readOnly = true)
    override fun getPosts(pageable: Pageable): Page<PostResponseDTO> {
        return postRepository.findAll(pageable).map { postResponseDTOFrom(it) }
    }

    @Cacheable(value = ["posts"], key = "#id")
    @Transactional(readOnly = true)
    override fun getPostById(id: Long): PostResponseDTO {
        val post = postRepository.findById(id).orElseThrow { NotFoundException("Post not found with id : $id") }
        return postResponseDTOFrom(post)
    }

    @Cacheable(value = ["commentsByPost"], key = "#id")
    @Transactional(readOnly = true)
    override fun getCommentsByPostId(id: Long, pageable: Pageable): Page<CommentResponseDTO> {
        postRepository.findById(id).orElseThrow { NotFoundException("Post not found with id : $id") }
        val comments = commentRepository.findByPostId(id, pageable)
        return comments.map { commentResponseDTOFrom(it) }
    }
}