package com.typebi.spring.comment.service

import com.typebi.spring.comment.requests.CommentCreateDTO
import com.typebi.spring.comment.requests.CommentUpdateDTO
import com.typebi.spring.comment.responses.CommentResponseDTO
import org.springframework.stereotype.Service

@Service
interface CommentService {
    fun createComment(commentCreateDTO: CommentCreateDTO): CommentResponseDTO
    fun getComments(): List<CommentResponseDTO>
    fun getCommentById(id: Long): CommentResponseDTO
    fun updateCommentById(id: Long, commentUpdateDTO: CommentUpdateDTO): CommentResponseDTO
    fun deleteCommentById(id: Long): Boolean

}