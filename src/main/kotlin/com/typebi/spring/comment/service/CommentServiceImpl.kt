package com.typebi.spring.comment.service

import com.typebi.spring.comment.requests.CommentCreateDTO
import com.typebi.spring.comment.requests.CommentUpdateDTO
import com.typebi.spring.comment.responses.CommentResponseDTO
import com.typebi.spring.comment.responses.commentResponseDTOFrom
import com.typebi.spring.common.exception.NotFoundException
import com.typebi.spring.comment.model.commentOf
import com.typebi.spring.comment.repository.CommentRepository
import com.typebi.spring.post.repository.PostRepository
import com.typebi.spring.user.repository.UserRepository
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CacheEvict
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val cacheManager: CacheManager
) : CommentService {

    @CacheEvict(value = ["commentsByPost"], key = "#commentCreateDTO.postId")
    @Transactional
    override fun createComment(commentCreateDTO: CommentCreateDTO): CommentResponseDTO {
        val post = postRepository.findById(commentCreateDTO.postId).orElseThrow { NotFoundException("Post not found with id : ${commentCreateDTO.postId}") }
        val author = userRepository.findById(commentCreateDTO.authorId).orElseThrow { NotFoundException("User not found with id : ${commentCreateDTO.authorId}") }
        return commentResponseDTOFrom(commentRepository.save(commentOf(commentCreateDTO, post, author)))
    }

    override fun getComments(): List<CommentResponseDTO> {
        return commentRepository.findAll().map { commentResponseDTOFrom(it) }
    }

    override fun getCommentById(id: Long): CommentResponseDTO {
        val comment = commentRepository.findById(id).orElseThrow { NotFoundException("Post not found with id : $id") }
        return commentResponseDTOFrom(comment)
    }

    @Transactional
    override fun updateCommentById(id: Long, commentUpdateDTO: CommentUpdateDTO): CommentResponseDTO {
        val comment = commentRepository.findById(id).orElseThrow { NotFoundException("Comment not found with id : $id") }

        var isCommentUpdated = false

        commentUpdateDTO.postId?.let {
            val post = postRepository.findById(it).orElseThrow { NotFoundException("Post not found with id : $it") }
            comment.post = post
            isCommentUpdated = true
        }
        commentUpdateDTO.content?.let {
            comment.content = it
            isCommentUpdated = true
        }
        commentUpdateDTO.authorId?.let {
            val author = userRepository.findById(it).orElseThrow { NotFoundException("User not found with id : $it") }
            comment.author = author
            isCommentUpdated = true
        }

        if (isCommentUpdated) {
            cacheManager.getCache("commentsByPost")?.evict(comment.post.id)
        }

        return commentResponseDTOFrom(comment)
    }

    @Transactional
    override fun deleteCommentById(id: Long): Boolean {
        val comment = commentRepository.findById(id).orElseThrow { NotFoundException("Comment not found with id : $id") }
        commentRepository.delete(comment)
        cacheManager.getCache("commentsByPost")?.evict(comment.post.id)
        return true
    }
}